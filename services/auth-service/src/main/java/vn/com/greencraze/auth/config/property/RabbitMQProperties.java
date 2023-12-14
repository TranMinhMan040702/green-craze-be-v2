package vn.com.greencraze.auth.config.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("rabbitmq")
public record RabbitMQProperties(
        String internalExchange,
        String mailRoutingKey
) {}
