package vn.com.greencraze.gateway.util;

import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import vn.com.greencraze.gateway.dto.SimpleRouteDefinition;
import vn.com.greencraze.gateway.exception.InvalidRequestException;

import java.util.List;
import java.util.function.Consumer;

public class RequestHelper {

    public static String extractJwtFromHeader(@Nullable String authorizationHeader) {
        if (!StringUtils.hasText(authorizationHeader)) {
            throw new InvalidRequestException("No authorization header");
        }

        if (!authorizationHeader.startsWith("Bearer ")) {
            throw new InvalidRequestException("Invalid authorization header");
        }

        return authorizationHeader.substring(7);
    }

    public static ServerWebExchange mutateHeaders(
            ServerWebExchange exchange, Consumer<HttpHeaders> headersConsumer) {
        return exchange.mutate().request(
                exchange.getRequest().mutate()
                        .headers(headersConsumer)
                        .build()
        ).build();
    }

    public static boolean isPrivate(ServerHttpRequest request, List<SimpleRouteDefinition> routeDefinitions) {
        return routeDefinitions.stream()
                .filter(routeDefinition -> routeDefinition.sourcePath().equals(request.getPath().toString()) &&
                        routeDefinition.method().equalsIgnoreCase(request.getMethod().name()))
                .findFirst()
                .map(routeDefinition -> "private".equals(routeDefinition.id().authLevel()))
                .orElse(false);
    }

}
