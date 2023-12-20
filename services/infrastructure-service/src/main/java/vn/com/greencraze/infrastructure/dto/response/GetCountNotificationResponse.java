package vn.com.greencraze.infrastructure.dto.response;

import lombok.Builder;

@Builder
public record GetCountNotificationResponse(
        long count
) {}
