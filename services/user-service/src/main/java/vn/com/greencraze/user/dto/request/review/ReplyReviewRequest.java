package vn.com.greencraze.user.dto.request.review;

import jakarta.validation.constraints.NotEmpty;

public record ReplyReviewRequest(
        @NotEmpty
        String reply
) {
        
}
