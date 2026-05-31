package com.api_gateway;

import jakarta.annotation.PostConstruct;

import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PermissionCacheService {

    private final DatabaseClient databaseClient;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    // role -> set of permissions
    private volatile Map<String, Set<String>> rolePermissionsCache = new HashMap<>();
    // userId -> set of permissions (for custom role)
    private volatile Map<String, Set<String>> userPermissionsCache = new HashMap<>();
    // list of route -> permission mappings
    private volatile List<RoutePermission> routePermissionsCache = new ArrayList<>();

    public PermissionCacheService(DatabaseClient databaseClient) {
        this.databaseClient = databaseClient;
    }

    @PostConstruct
    public void init() {
        refreshCache()
                .doOnError(e -> System.err
                        .println("Warning: Could not load permission cache on startup: " + e.getMessage()))
                .onErrorComplete() // don't crash startup if DB is unreachable
                .subscribe(); // non-blocking, fire and forget
    }

    @Scheduled(fixedDelay = 5*1000) // refresh every 5 seconds
    public Mono<Void> refreshCache() {
        return Mono.zip(
                loadRolePermissions(),
                loadUserPermissions()).doOnNext(tuple -> {
                    rolePermissionsCache = tuple.getT1();
                    userPermissionsCache = tuple.getT2();
                    System.out.println("Permission cache refreshed");
                }).then();
    }

    private Mono<Map<String, Set<String>>> loadRolePermissions() {
        return databaseClient
                .sql("SELECT role::text, permission::text FROM users_svc.role_permissions")
                .fetch().all()
                .collectMultimap(
                        row -> (String) row.get("role"),
                        row -> (String) row.get("permission"))
                .map(map -> map.entrySet().stream()
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                e -> new HashSet<>(e.getValue()))));
    }

    private Mono<Map<String, Set<String>>> loadUserPermissions() {
        return databaseClient
                .sql("SELECT user_id::text, permission::text FROM users_svc.user_permissions")
                .fetch().all()
                .collectMultimap(
                        row -> (String) row.get("user_id"),
                        row -> (String) row.get("permission"))
                .map(map -> map.entrySet().stream()
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                e -> new HashSet<>(e.getValue()))));
    }

    public boolean hasPermission(String role, String userId, String permission) {
        // Check via role
        System.out.println("permission being checked: "+permission);
        System.out.println("role: "+role);
        Set<String> rolePerms = rolePermissionsCache.getOrDefault(role, Set.of());
        if (rolePerms.contains(permission))
            return true;
        System.out.println("roles found for role: "+role);
        for (String val : rolePerms) {
            System.out.println(val);
        }
        System.out.println("could not found in default permissions checking custom role");

        // Check via direct user permission override (handles custom role)
        Set<String> userPerms = userPermissionsCache.getOrDefault(userId, Set.of());
        return userPerms.contains(permission);
    }

    public Optional<RoutePermission> resolveRoute(String method, String path) {
        return routePermissionsCache.stream()
                .filter(r -> r.getMethod().equals(method) &&
                        pathMatcher.match(r.getPathPattern(), path))
                .findFirst();
    }

    public Mono<Void> forceRefresh() {
        return refreshCache();
    }

    // Inner class
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