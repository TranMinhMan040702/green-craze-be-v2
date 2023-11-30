package vn.com.greencraze.user.dto.request.userFollowProduct;

import jakarta.validation.constraints.NotNull;

public record FollowProductRequest(
        @NotNull
        Long productId
) {
        
}
