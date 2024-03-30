package vn.com.greencraze.infrastructure.dto.response.notification;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.lang.Nullable;
import vn.com.greencraze.commons.enumeration.NotificationType;

import java.time.Instant;

public record GetListNotificationResponse(
        Long id,
        Instant createdAt,
        Instant updatedAt,
        @Nullable
        @Schema(nullable = true)
        String createdBy,
        @Nullable
        @Schema(nullable = true)
        String updatedBy,
        NotificationType type,
        String content,
        String title,
        String anchor,
        String image,
        Boolean status
) {}
