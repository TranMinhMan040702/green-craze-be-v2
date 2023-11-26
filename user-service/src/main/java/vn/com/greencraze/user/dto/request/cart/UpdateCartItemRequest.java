package vn.com.greencraze.user.dto.request.cart;

import jakarta.validation.constraints.NotNull;

public record UpdateCartItemRequest(
        @NotNull
        Integer quantity
) {

}
