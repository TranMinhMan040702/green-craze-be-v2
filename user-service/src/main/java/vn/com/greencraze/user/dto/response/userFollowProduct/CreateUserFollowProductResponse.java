package vn.com.greencraze.user.dto.response.userFollowProduct;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.lang.Nullable;

import java.time.Instant;

public record CreateUserFollowProductResponse(
        Long id,
        Instant createdAt,
        Instant updatedAt,
        @Nullable
        @Schema(nullable = true)
        String createdBy,
        @Nullable
        @Schema(nullable = true)
        String updatedBy,
        Long productId,
        Long orderItemId,
        String title,
        String content,
        Integer rating,
        String image,
        String reply,
        Boolean status
) {
        
}
