package com.api_gateway;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.stereotype.Component;

@Component
public class PermissionAuthGatewayFilterFactory
        extends AbstractGatewayFilterFactory<PermissionAuthGatewayFilterFactory.Config> {

    private static final Logger log = LoggerFactory.getLogger(PermissionAuthGatewayFilterFactory.class);

    private final ReactiveJwtDecoder jwtDecoder;
    private final PermissionCacheService permissionCache;

    public PermissionAuthGatewayFilterFactory(
            ReactiveJwtDecoder jwtDecoder,
            PermissionCacheService permissionCache) {
        super(Config.class);
        this.jwtDecoder = jwtDecoder;
        this.permissionCache = permissionCache;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            if (exchange.getRequest().getMethod() == HttpMethod.OPTIONS) {
                return chain.filter(exchange);
            }

            String authorizationHeader = exchange.getRequest()
                    .getHeaders()
                    .getFirst("Authorization");

            if (authorizationHeader == null
                    || !authorizationHeader.startsWith("Bearer ")
                    || authorizationHeader.length() <= 7) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            String token = authorizationHeader.substring(7).trim();
            if (token.isEmpty()) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            List<String> requiredPermissions = config.getPermissions();
            if (requiredPermissions == null || requiredPermissions.isEmpty()) {
                log.error("PermissionAuth route has no permissions configured");
                exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                return exchange.getResponse().setComplete();
            }

            return jwtDecoder.decode(token)
                    .flatMap(jwt -> {
                        // With the chosen design, JWT sub == users_svc.users.auth_id.
                        String authId = jwt.getSubject();

                        if (authId == null || authId.isBlank()) {
                            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                            return exchange.getResponse().setComplete();
                        }

                        // Resolve the application role from the Users database cache.
                        String userRole = permissionCache.getUserRole(authId).orElse(null);

                        if (userRole == null) {
                            log.warn("No application user/role mapping found for Keycloak sub {}", authId);
                            exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                            return exchange.getResponse().setComplete();
                        }

                        // OR semantics: any one configured permission grants access.
                        boolean allowed = requiredPermissions.stream()
                                .filter(permission -> permission != null && !permission.isBlank())
                                .anyMatch(permission -> permissionCache.hasPermission(
                                        userRole,
                                        authId,
                                        permission));

                        if (!allowed) {
                            log.debug(
                                    "Permission denied for auth_id {} (role {}); required any of {}",
                                    authId,
                                    userRole,
                                    requiredPermissions);
                            exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                            return exchange.getResponse().setComplete();
                        }

                        // Never trust identity headers supplied by the external client.
                        // Replace them with values derived from the validated JWT/database lookup.
                        return chain.filter(exchange.mutate()
                                .request(request -> request.headers(headers -> {
                                    headers.remove("X-User-Id");
                                    headers.remove("X-User-Role");
                                    headers.set("X-User-Id", authId);
                                    headers.set("X-User-Role", userRole);
                                }))
                                .build());
                    })
                    // Only JWT validation/decoding failures are translated to 401.
                    // Unexpected infrastructure/programming errors should not be mislabeled as 401.
                    .onErrorResume(JwtException.class, error -> {
                        log.warn("JWT validation failed: {}", error.getMessage());
                        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                        return exchange.getResponse().setComplete();
                    });
        };
    }

    public static class Config {
        private List<String> permissions = new ArrayList<>();

        public List<String> getPermissions() {
            return permissions;
        }

        public void setPermissions(List<String> permissions) {
            this.permissions = permissions;
        }
    }
}
