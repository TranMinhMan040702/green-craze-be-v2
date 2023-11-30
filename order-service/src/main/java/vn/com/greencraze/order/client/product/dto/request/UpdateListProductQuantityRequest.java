package vn.com.greencraze.order.client.product.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record UpdateListProductQuantityRequest(
        @NotNull
        @NotEmpty
        List<ProductQuantityItem> quantityItems
) {

    public record ProductQuantityItem(
            Long id,
            Integer quantity
    ) {
        
    }

}
