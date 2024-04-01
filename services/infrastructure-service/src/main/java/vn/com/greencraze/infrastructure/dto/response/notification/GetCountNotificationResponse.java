package vn.com.greencraze.infrastructure.dto.response.notification;

import lombok.Builder;

@Builder
public record GetCountNotificationResponse(
        long count
) {}
