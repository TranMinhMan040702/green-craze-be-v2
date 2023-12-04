package vn.com.greencraze.infrastructure.config.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.exchanges.topic}")
    private String internalExchange;

    @Value("${rabbitmq.queues.mail}")
    private String mailQueue;

    @Value("${rabbitmq.routing-key.mail}")
    private String mailRoutingKey;

    @Bean
    public DirectExchange internalExchange() {
        return new DirectExchange(this.internalExchange);
    }

    @Bean
    public Queue mailQueue() {
        return new Queue(this.mailQueue);
    }

    @Bean
    public Binding mailBinding() {
        return BindingBuilder
                .bind(mailQueue())
                .to(internalExchange())
                .with(this.mailRoutingKey);
    }

}
