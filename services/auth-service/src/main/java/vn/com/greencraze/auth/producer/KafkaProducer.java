package vn.com.greencraze.auth.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import vn.com.greencraze.commons.domain.dto.SendEmailRequest;
import vn.com.greencraze.commons.domain.topic.Topics;

@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendMail(String key, SendEmailRequest payload) {
        log.debug("sending to mail topic={}, key={}, topic={}", payload, key, Topics.MAIL);
        kafkaTemplate.send(Topics.MAIL, key, payload);
    }

}
