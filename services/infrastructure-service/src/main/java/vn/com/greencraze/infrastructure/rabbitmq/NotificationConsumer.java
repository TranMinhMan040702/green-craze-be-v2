package vn.com.greencraze.infrastructure.rabbitmq;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import vn.com.greencraze.infrastructure.dto.request.CreateNotificationRequest;
import vn.com.greencraze.infrastructure.service.INotificationService;

@Component
@Slf4j
@RequiredArgsConstructor
public class NotificationConsumer {

    private final INotificationService notificationService;

    @RabbitListener(queues = "${rabbitmq.queues.notification}")
    public void consumer(CreateNotificationRequest request) {
        log.info("Consumed {} from queue", request);
        notificationService.createNotification(request);
    }

}
