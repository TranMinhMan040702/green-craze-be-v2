package vn.com.greencraze.infrastructure.dto.response.room;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.lang.Nullable;

import java.time.Instant;
import java.util.List;

public record GetOneRoomByUserIdResponse(
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
        String name,
        List<MessageResponse> messages
) {

    public record MessageResponse(
            Long id,
            String image,
            Boolean status,
            String content
    ) {}

}
