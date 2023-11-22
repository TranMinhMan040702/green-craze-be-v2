package vn.com.greencraze.user.dto.request.review;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotEmpty;

public record ReplyReviewRequest(
        @JsonIgnore
        Long id,
        @NotEmpty
        String reply
) {
    public ReplyReviewRequest setId(Long id) {
        return new ReplyReviewRequest(id, reply());
    }
}
