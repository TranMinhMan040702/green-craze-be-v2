package vn.com.greencraze.infrastructure.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import org.springframework.lang.Nullable;

import java.time.Instant;

@Builder
public record MessageResponse(
        Long id,
        Instant createdAt,
        Instant updatedAt,
        @Nullable
        @Schema(nullable = true)
        String createdBy,
        @Nullable
        @Schema(nullable = true)
        String updatedBy,
        String userId,
        Long roomId,
        String image,
        Boolean status,
        String content
) {}
