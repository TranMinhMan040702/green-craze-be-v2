package vn.com.greencraze.infrastructure.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import vn.com.greencraze.commons.domain.dto.CreateNotificationRequest;
import vn.com.greencraze.commons.domain.id.KafkaGroupIds;
import vn.com.greencraze.commons.domain.topic.Topics;
import vn.com.greencraze.infrastructure.service.INotificationService;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationListener {

    private final INotificationService notificationService;

    @KafkaListener(topics = Topics.NOTIFICATION, groupId = KafkaGroupIds.NOTIFICATION)
    public void consumer(CreateNotificationRequest request) {
        log.info("Received: {}", request);
        notificationService.createNotification(request);
    }

}
