package vn.com.greencraze.infrastructure.dto.response.notification;

import lombok.Builder;

@Builder
public record CreateNotificationResponse(
        String title,
        String content,
        Long count
) {}
