package vn.com.greencraze.gateway.config;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

@Configuration
@RequiredArgsConstructor
public class GatewayConfig {

    private final GatewayProperties gatewayProperties;

    private final HttpClient httpClient;

    @Bean
    @LoadBalanced
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder().clientConnector(new ReactorClientHttpConnector(httpClient));
    }

    //    @Bean
    //    public List<SimpleRouteDefinition> simpleRouteDefinitions() {
    //        return gatewayProperties.getRoutes().stream()
    //                .map(RouteHelper::mapToSimpleRouteDefinition)
    //                .toList();
    //    }

}
