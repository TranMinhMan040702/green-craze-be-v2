package vn.com.greencraze.gateway.filter;


import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import vn.com.greencraze.gateway.client.AuthServiceClient;
import vn.com.greencraze.gateway.dto.ValidateAccessTokenRequest;
import vn.com.greencraze.gateway.util.CustomHeaders;
import vn.com.greencraze.gateway.util.RequestHelper;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class PreFilter {

    private final AuthServiceClient authServiceClient;

    @Bean
    @Order(-2)
    public GlobalFilter authFilter() {
        return (exchange, chain) -> {
            Route route = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
            ServerHttpRequest request = exchange.getRequest();
            HttpHeaders headers = request.getHeaders();

            if (RequestHelper.isPrivate(route)) {
                ValidateAccessTokenRequest validateAccessTokenRequest = ValidateAccessTokenRequest.builder()
                        .accessToken(RequestHelper.extractJwtFromHeader(headers.getFirst(HttpHeaders.AUTHORIZATION)))
                        .build();

                return authServiceClient
                        .validateAccessToken(validateAccessTokenRequest)
                        .map(response -> RequestHelper.mutateHeaders(exchange, h -> h.setAll(
                                Map.of(
                                        CustomHeaders.X_AUTH_USER_ID, response.data().userId(),
                                        CustomHeaders.X_AUTH_USER_AUTHORITIES, String
                                                .join(",", response.data().userAuthorities())
                                ))))
                        .flatMap(chain::filter)
                        .onErrorResume(Mono::error);
            }

            return chain.filter(exchange);
        };
    }

}
