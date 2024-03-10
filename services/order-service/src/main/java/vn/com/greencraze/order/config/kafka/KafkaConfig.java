package vn.com.greencraze.order.config.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import vn.com.greencraze.commons.domain.topic.Topics;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic mailTopic() {
        return TopicBuilder.name(Topics.MAIL)
                .partitions(1)
                .compact()
                .build();
    }

    @Bean
    public NewTopic reduceStockTopic() {
        return TopicBuilder.name(Topics.REDUCE_STOCK)
                .partitions(1)
                .compact()
                .build();
    }

    @Bean
    public NewTopic reduceStockReplyTopic() {
        return TopicBuilder.name(Topics.REDUCE_STOCK_REPLY)
                .partitions(1)
                .compact()
                .build();
    }

    @Bean
    public NewTopic revertStockTopic() {
        return TopicBuilder.name(Topics.REVERT_STOCK)
                .partitions(1)
                .compact()
                .build();
    }

    @Bean
    public NewTopic revertStockReplyTopic() {
        return TopicBuilder.name(Topics.REVERT_STOCK_REPLY)
                .partitions(1)
                .compact()
                .build();
    }

    @Bean
    public NewTopic createDocketTopic() {
        return TopicBuilder.name(Topics.CREATE_DOCKET)
                .partitions(1)
                .compact()
                .build();
    }

    @Bean
    public NewTopic createDocketReplyTopic() {
        return TopicBuilder.name(Topics.CREATE_DOCKET_REPLY)
                .partitions(1)
                .compact()
                .build();
    }

    @Bean
    public NewTopic revertDocketTopic() {
        return TopicBuilder.name(Topics.REVERT_DOCKET)
                .partitions(1)
                .compact()
                .build();
    }

    @Bean
    public NewTopic revertDocketReplyTopic() {
        return TopicBuilder.name(Topics.REVERT_DOCKET_REPLY)
                .partitions(1)
                .compact()
                .build();
    }

    @Bean
    public NewTopic cartClearTopic() {
        return TopicBuilder.name(Topics.CART_CLEAR)
                .partitions(1)
                .compact()
                .build();
    }

    @Bean
    public NewTopic cartClearReplyTopic() {
        return TopicBuilder.name(Topics.CART_CLEAR_REPLY)
                .partitions(1)
                .compact()
                .build();
    }

}
