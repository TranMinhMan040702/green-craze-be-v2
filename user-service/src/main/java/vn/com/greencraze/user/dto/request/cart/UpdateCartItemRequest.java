package vn.com.greencraze.user.dto.request.cart;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotNull;

public record UpdateCartItemRequest(
        @JsonIgnore
        String userId,
        @JsonIgnore
        Long cartItemId,
        @NotNull
        Integer quantity
) {
    public UpdateCartItemRequest setUserIdAndCartItemId(String userId, Long cartItemId) {
        return new UpdateCartItemRequest(userId, cartItemId, quantity() < 1 ? 1 : quantity());
    }
}
