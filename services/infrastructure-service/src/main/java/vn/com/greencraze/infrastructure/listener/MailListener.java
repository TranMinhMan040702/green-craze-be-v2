package vn.com.greencraze.infrastructure.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import vn.com.greencraze.commons.domain.KafkaGroupIds;
import vn.com.greencraze.commons.domain.KafkaIds;
import vn.com.greencraze.commons.domain.Topics;
import vn.com.greencraze.commons.domain.dto.SendEmailRequest;
import vn.com.greencraze.infrastructure.service.IMailService;

@Slf4j
@Component
@RequiredArgsConstructor
public class MailListener {

    private final IMailService mailService;

    @KafkaListener(id = KafkaIds.INFRASTRUCTURE, topics = Topics.MAIL, groupId = KafkaGroupIds.MAIL)
    public void consumer(SendEmailRequest request) {
        log.info("Received: {}", request);
        mailService.sendEmail(request);
    }

}
