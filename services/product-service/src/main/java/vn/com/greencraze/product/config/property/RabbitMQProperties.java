package vn.com.greencraze.product.config.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("rabbitmq")
public record RabbitMQProperties(
        String internalExchange,
        String notificationRoutingKey
) {}
