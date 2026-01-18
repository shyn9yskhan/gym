package com.shyn9yskhan.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import org.springframework.security.oauth2.jwt.Jwt;

@Component
public class GlobalAuthFilter implements GlobalFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .filter(auth -> auth.getPrincipal() instanceof Jwt)
                .map(auth -> (Jwt) auth.getPrincipal())
                .map(jwt -> {
                    String userId = jwt.getSubject();
                    String role = jwt.getClaimAsString("role");

                    String secondId = null;
                    if ("TRAINEE".equals(role)) {
                        secondId = jwt.getClaimAsString("traineeId");
                    } else if ("TRAINER".equals(role)) {
                        secondId = jwt.getClaimAsString("trainerId");
                    }

                    ServerHttpRequest request = exchange.getRequest()
                            .mutate()
                            .header("X-User-Id", userId)
                            .header("X-User-Role", role)
                            .header("X-User-Second-Id", secondId != null ? secondId : "")
                            .build();

                    return exchange.mutate().request(request).build();
                })
                .defaultIfEmpty(exchange)
                .flatMap(chain::filter);
    }
}
