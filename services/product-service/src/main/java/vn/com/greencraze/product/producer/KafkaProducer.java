package vn.com.greencraze.product.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import vn.com.greencraze.commons.domain.Topics;
import vn.com.greencraze.commons.domain.dto.CreateNotificationRequest;

@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendNotification(String key, CreateNotificationRequest payload) {
        log.debug("sending to notification topic={}, key={}, topic={}", payload, key, Topics.NOTIFICATION);
        kafkaTemplate.send(Topics.NOTIFICATION, key, payload);
    }

}
