package vn.com.greencraze.order.rabbitmq.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import vn.com.greencraze.commons.enumeration.NotificationType;

@Builder
public record CreateNotificationRequest(
        String userId,
        @NotNull
        NotificationType type,
        @NotNull
        String content,
        @NotNull
        String title,
        @NotNull
        String anchor,
        @NotNull
        String image
) {}
