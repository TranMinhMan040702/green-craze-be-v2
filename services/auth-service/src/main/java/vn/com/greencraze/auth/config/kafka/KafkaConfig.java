package vn.com.greencraze.auth.config.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import vn.com.greencraze.commons.domain.Topics;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic mailTopic() {
        return TopicBuilder.name(Topics.MAIL)
                .partitions(1)
                .compact()
                .build();
    }

}
