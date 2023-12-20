package vn.com.greencraze.order.config.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("rabbitmq")
public record RabbitMQProperties(
        String internalExchange,
        String mailRoutingKey,
        String notificationRoutingKey
) {}
