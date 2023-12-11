package vn.com.greencraze.infrastructure.dto.response;

import lombok.Builder;

@Builder
public record CreateNotificationResponse(
        String title,
        String content,
        Long count
) {}
