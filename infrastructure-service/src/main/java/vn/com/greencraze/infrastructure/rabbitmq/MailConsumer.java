package vn.com.greencraze.infrastructure.rabbitmq;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import vn.com.greencraze.infrastructure.dto.request.SendEmailRequest;
import vn.com.greencraze.infrastructure.service.IMailService;

@Component
@Slf4j
@RequiredArgsConstructor
public class MailConsumer {

    private final IMailService mailService;

    @RabbitListener(queues = "${rabbitmq.queues.mail}")
    public void consumer(SendEmailRequest request) {
        log.info("Consumed {} from queue", request);
        mailService.sendEmail(request);
    }

}
