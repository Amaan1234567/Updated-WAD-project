package com.api_gateway;

import java.util.ArrayList;
import java.util.List;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.stereotype.Component;

@Component
public class PermissionAuthGatewayFilterFactory
        extends AbstractGatewayFilterFactory<PermissionAuthGatewayFilterFactory.Config> {

    private final ReactiveJwtDecoder jwtDecoder;
    private final PermissionCacheService permissionCache;

    public PermissionAuthGatewayFilterFactory(ReactiveJwtDecoder jwtDecoder,
            PermissionCacheService permissionCache) {
        super(Config.class);
        this.jwtDecoder = jwtDecoder;
        this.permissionCache = permissionCache;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String authHeader = exchange.getRequest()
                    .getHeaders()
                    .getFirst("Authorization");

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            String token = authHeader.substring(7);

            return jwtDecoder.decode(token)
                    .flatMap(jwt -> {
                        String userRole = jwt.getClaimAsString("user_role");
                        String userId = jwt.getSubject();
                        boolean hasAtleastOnePermission = false;
                        for (String permission : config.getPermissions()) {
                            hasAtleastOnePermission = hasAtleastOnePermission || permissionCache.hasPermission(userRole, userId, permission);
                        }
                        if (!hasAtleastOnePermission) {
                            exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                            return exchange.getResponse().setComplete();
                        }

                        return chain.filter(exchange.mutate()
                                .request(r -> r
                                        .header("X-User-Id", userId)
                                        .header("X-User-Role", userRole))
                                .build());
                    })
                    .onErrorResume(e -> {
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