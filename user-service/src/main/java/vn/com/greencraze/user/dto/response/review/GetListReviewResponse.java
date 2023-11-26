package vn.com.greencraze.user.dto.response.review;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.lang.Nullable;
import vn.com.greencraze.user.entity.UserProfile;

import java.time.Instant;

public record GetListReviewResponse(
        Long id,
        Instant createdAt,
        Instant updatedAt,
        @Nullable
        @Schema(nullable = true)
        String createdBy,
        @Nullable
        @Schema(nullable = true)
        String updatedBy,
        String UserId,
        Long productId,
        Long orderItemId,
        String title,
        String content,
        Integer rating,
        String image,
        String reply,
        Boolean status,
        String variantName,
//        ProductDto Product,
        UserProfile user
) {
}
