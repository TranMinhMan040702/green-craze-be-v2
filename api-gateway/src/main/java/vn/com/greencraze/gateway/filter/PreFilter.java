package vn.com.greencraze.gateway.filter;


import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import vn.com.greencraze.gateway.client.AuthServiceClient;
import vn.com.greencraze.gateway.dto.SimpleRouteDefinition;
import vn.com.greencraze.gateway.dto.ValidateAccessTokenRequest;
import vn.com.greencraze.gateway.exception.TokenValidationException;
import vn.com.greencraze.gateway.util.CustomHeaders;
import vn.com.greencraze.gateway.util.RequestHelper;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class PreFilter implements WebFilter {

    private final List<SimpleRouteDefinition> simpleRouteDefinitions;

    private final AuthServiceClient authServiceClient;

    @Override
    @NotNull
    public Mono<Void> filter(ServerWebExchange exchange, @NotNull WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        HttpHeaders headers = request.getHeaders();

        if (RequestHelper.isPrivate(request, simpleRouteDefinitions)) {
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
                    .onErrorResume(error -> Mono.error(new TokenValidationException((error.getMessage()))));
        }

        return chain.filter(exchange);
    }

}
