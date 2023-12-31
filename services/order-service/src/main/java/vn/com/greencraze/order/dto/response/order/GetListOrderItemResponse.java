package vn.com.greencraze.order.dto.response.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.With;
import org.springframework.lang.Nullable;

import java.math.BigDecimal;
import java.time.Instant;

@With
public record GetListOrderItemResponse(
        Long id,
        Instant createdAt,
        Instant updatedAt,
        @Nullable
        @Schema(nullable = true)
        String createdBy,
        @Nullable
        @Schema(nullable = true)
        String updatedBy,
        Long variantId,
        Integer quantity,
        BigDecimal unitPrice,
        BigDecimal totalPrice,
        String sku,
        Long productId,
        String productSlug,
        String productUnit,
        String productImage,
        String productName,
        String variantName,
        Long variantQuantity
) {}
