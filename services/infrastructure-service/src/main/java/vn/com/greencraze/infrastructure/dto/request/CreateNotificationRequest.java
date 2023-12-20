package vn.com.greencraze.infrastructure.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.With;
import vn.com.greencraze.commons.enumeration.NotificationType;

@With
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
