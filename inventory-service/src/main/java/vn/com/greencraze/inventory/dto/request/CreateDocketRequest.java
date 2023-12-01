package vn.com.greencraze.inventory.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateDocketRequest(
        @NotNull
        Long orderId,
        @NotBlank
        String type,
        @NotNull
        @NotEmpty
        List<ProductDocket> productDockets
) {

    public record ProductDocket(
            @NotNull
            Long productId,
            @NotNull
            Integer quantity
    ) {

    }

}