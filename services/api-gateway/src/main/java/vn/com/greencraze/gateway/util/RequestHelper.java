package vn.com.greencraze.gateway.util;

import org.springframework.cloud.gateway.route.Route;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import vn.com.greencraze.gateway.exception.InvalidRequestException;

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

    public static boolean isPrivate(@Nullable Route route) {
        return route != null && route.getId().startsWith("private");
    }

}
