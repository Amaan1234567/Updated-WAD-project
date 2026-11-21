package com.api_gateway;

import jakarta.annotation.PostConstruct;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;

import reactor.core.publisher.Mono;

@Service
public class PermissionCacheService {

    private static final Logger log = LoggerFactory.getLogger(PermissionCacheService.class);

    private static final Set<String> ROLE_BASED_ROLES = Set.of("ADMIN", "USER", "CSR");

    private final DatabaseClient databaseClient;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    /*
     * All permission data is published as one snapshot. This prevents a request
     * from seeing a mixture of old and newly refreshed maps.
     */
    private volatile CacheSnapshot cache = CacheSnapshot.empty();

    /*
     * Kept for the existing resolveRoute() API. This list is not populated by
     * the current database queries; route authorization is currently configured
     * through Spring Cloud Gateway's application.yml filters.
     */
    private volatile List<RoutePermission> routePermissionsCache = List.of();

    public PermissionCacheService(DatabaseClient databaseClient) {
        this.databaseClient = databaseClient;
    }

    @PostConstruct
    public void init() {
        refreshCache().subscribe(
                ignored -> {
                },
                error -> log.warn("Could not load permission cache during startup", error));
    }

    @Scheduled(fixedDelay = 5_000)
    public Mono<Void> refreshCache() {
        return Mono.zip(
                loadRolePermissions(),
                loadUserPermissions(),
                loadUserRoles())
                .doOnNext(tuple -> {
                    cache = new CacheSnapshot(
                            tuple.getT1(),
                            tuple.getT2(),
                            tuple.getT3());
                    log.info(
                            "Permission cache refreshed: {} roles, {} custom users, {} user-role mappings",
                            cache.rolePermissions().size(),
                            cache.userPermissions().size(),
                            cache.userRoles().size());
                })
                .then()
                .doOnError(error -> log.error("Permission cache refresh failed; keeping previous cache", error));
    }

    /**
     * Loads the default permissions assigned to each application role.
     * Expected roles include ADMIN, USER and CSR. CUSTOM users do not use this map.
     */
    private Mono<Map<String, Set<String>>> loadRolePermissions() {
        return databaseClient
                .sql("""
                        SELECT
                            role::text AS role,
                            permission::text AS permission
                        FROM users_svc.role_permissions
                        """)
                .fetch()
                .all()
                .collectMultimap(
                        row -> normalizeRole((String) row.get("role")),
                        row -> (String) row.get("permission"))
                .map(PermissionCacheService::toImmutablePermissionMap);
    }

    /**
     * Loads individually assigned permissions for CUSTOM users.
     *
     * Assumption based on the supplied schema details:
     * users_svc.user_permissions.user_id is UUID and stores the same UUID as
     * users_svc.users.auth_id (which is now populated with the Keycloak JWT sub).
     */
    private Mono<Map<String, Set<String>>> loadUserPermissions() {
        return databaseClient
                .sql("""
                        SELECT
                            user_id::text AS auth_id,
                            permission::text AS permission
                        FROM users_svc.user_permissions
                        """)
                .fetch()
                .all()
                .collectMultimap(
                        row -> normalizeAuthId((String) row.get("auth_id")),
                        row -> (String) row.get("permission"))
                .map(PermissionCacheService::toImmutablePermissionMap);
    }

    /**
     * Maps Keycloak subject (JWT sub) to the user's application role.
     * The application stores that subject in users_svc.users.auth_id.
     */
    private Mono<Map<String, String>> loadUserRoles() {
        return databaseClient
                .sql("""
                        SELECT
                            auth_id::text AS auth_id,
                            user_type::text AS role
                        FROM users_svc.users
                        WHERE auth_id IS NOT NULL
                          AND user_type IS NOT NULL
                        """)
                .fetch()
                .all()
                .collectMap(
                        row -> normalizeAuthId((String) row.get("auth_id")),
                        row -> normalizeRole((String) row.get("role")))
                .map(Map::copyOf);
    }

    /**
     * Resolves the application's role using the Keycloak JWT subject.
     * auth_id is expected to contain that same UUID.
     */
    public Optional<String> getUserRole(String keycloakSubject) {
        if (keycloakSubject == null || keycloakSubject.isBlank()) {
            return Optional.empty();
        }

        return Optional.ofNullable(
                cache.userRoles().get(normalizeAuthId(keycloakSubject)));
    }

    /**
     * CUSTOM users use only userPermissionsCache.
     * ADMIN, USER and CSR use only rolePermissionsCache.
     * There is deliberately no fallback between the two permission sources.
     */
    public boolean hasPermission(String role, String authId, String permission) {
        if (role == null || role.isBlank()
                || authId == null || authId.isBlank()
                || permission == null || permission.isBlank()) {
            return false;
        }

        String normalizedRole = normalizeRole(role);
        String normalizedAuthId = normalizeAuthId(authId);
        CacheSnapshot currentCache = cache;

        if ("CUSTOM".equals(normalizedRole)) {
            Set<String> userPermissions = currentCache.userPermissions()
                    .getOrDefault(normalizedAuthId, Set.of());
            return userPermissions.contains(permission);
        }

        if (!ROLE_BASED_ROLES.contains(normalizedRole)) {
            log.warn("Unknown application role '{}' for auth_id {}", normalizedRole, normalizedAuthId);
            return false;
        }

        Set<String> rolePermissions = currentCache.rolePermissions()
                .getOrDefault(normalizedRole, Set.of());
        return rolePermissions.contains(permission);
    }

    public Optional<RoutePermission> resolveRoute(String method, String path) {
        return routePermissionsCache.stream()
                .filter(route -> route.getMethod().equals(method)
                        && pathMatcher.match(route.getPathPattern(), path))
                .findFirst();
    }

    public Mono<Void> forceRefresh() {
        return refreshCache();
    }

    private static String normalizeRole(String role) {
        if (role == null || role.isBlank()) {
            throw new IllegalArgumentException("Application role must not be blank");
        }
        return role.trim().toUpperCase(Locale.ROOT);
    }

    private static String normalizeAuthId(String authId) {
        if (authId == null || authId.isBlank()) {
            throw new IllegalArgumentException("Authentication ID must not be blank");
        }
        // auth_id is PostgreSQL UUID, whose text representation is canonical lowercase.
        return authId.trim().toLowerCase(Locale.ROOT);
    }

    private static Map<String, Set<String>> toImmutablePermissionMap(
            Map<String, Collection<String>> groupedPermissions) {
        Map<String, Set<String>> result = new HashMap<>();
        groupedPermissions.forEach((key, values) -> result.put(key, Set.copyOf(values)));
        return Map.copyOf(result);
    }

    private record CacheSnapshot(
            Map<String, Set<String>> rolePermissions,
            Map<String, Set<String>> userPermissions,
            Map<String, String> userRoles) {

        private static CacheSnapshot empty() {
            return new CacheSnapshot(Map.of(), Map.of(), Map.of());
        }
    }

    public static class RoutePermission {
        private final String method;
        private final String pathPattern;
        private final String permission;
        private final boolean isPublic;

        public RoutePermission(String method, String pathPattern,
                String permission, boolean isPublic) {
            this.method = method;
            this.pathPattern = pathPattern;
            this.permission = permission;
            this.isPublic = isPublic;
        }

        public String getMethod() {
            return method;
        }

        public String getPathPattern() {
            return pathPattern;
        }

        public String getPermission() {
            return permission;
        }

        public boolean isPublic() {
            return isPublic;
        }
    }
}
