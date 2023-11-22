package vn.com.greencraze.user.dto.request.review;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record UpdateReviewRequest(
        @JsonIgnore
        Long id,
        @NotEmpty
        String title,
        @NotEmpty
        String content,
        @NotNull
        Integer rating,
        @NotNull
        MultipartFile image,
        Boolean isDeleteImage
) {
    public UpdateReviewRequest setId(Long id) {
        return new UpdateReviewRequest(id, title(), content(), rating(), image(), isDeleteImage());
    }
}
