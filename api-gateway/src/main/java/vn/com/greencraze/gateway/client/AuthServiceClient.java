package vn.com.greencraze.gateway.client;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import vn.com.greencraze.gateway.dto.RestResponse;
import vn.com.greencraze.gateway.dto.ValidateAccessTokenRequest;
import vn.com.greencraze.gateway.dto.ValidateAccessTokenResponse;

@Component
@RequiredArgsConstructor
public class AuthServiceClient {

    private static final String HOST = "lb://auth-service/core/auth/auths";

    private final WebClient.Builder webClientBuilder;

    public Mono<RestResponse<ValidateAccessTokenResponse>> validateAccessToken(ValidateAccessTokenRequest request) {
        return webClientBuilder
                .baseUrl(HOST)
                .build()
                .post()
                .uri("/validate-access-token")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<>() {});
    }

}
