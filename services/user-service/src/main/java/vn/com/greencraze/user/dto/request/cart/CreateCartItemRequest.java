package vn.com.greencraze.user.dto.request.cart;

import jakarta.validation.constraints.NotNull;

public record CreateCartItemRequest(
        @NotNull
        Long variantId,
        @NotNull
        Integer quantity
) {
        
}
