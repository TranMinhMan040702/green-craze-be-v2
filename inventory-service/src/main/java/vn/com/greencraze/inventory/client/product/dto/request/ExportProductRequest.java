package vn.com.greencraze.inventory.client.product.dto.request;

import lombok.Builder;

@Builder
public record ExportProductRequest(
        Long id,
        Long quantity
) {}
