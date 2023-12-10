package vn.com.greencraze.inventory.client.product.dto.request;

import lombok.Builder;

@Builder
public record ImportProductRequest(
        Long id,
        Long quantity,
        Long actualInventory
) {}
