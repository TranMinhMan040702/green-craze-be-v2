package vn.com.greencraze.user.dto.request.review;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record CreateReviewRequest(
        @NotNull
        Long productId,
        @NotNull
        Long orderItemId,
        @NotEmpty
        String title,
        @NotEmpty
        String content,
        @NotNull
        Integer rating,
        @NotNull
        MultipartFile image
) {
        
}
