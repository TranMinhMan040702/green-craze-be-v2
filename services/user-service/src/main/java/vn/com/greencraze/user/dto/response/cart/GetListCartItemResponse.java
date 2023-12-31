package vn.com.greencraze.user.dto.response.cart;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.With;
import org.springframework.lang.Nullable;

import java.math.BigDecimal;
import java.time.Instant;

@With
public record GetListCartItemResponse(
        Long id,
        Instant createdAt,
        Instant updatedAt,
        @Nullable
        @Schema(nullable = true)
        String createdBy,
        @Nullable
        @Schema(nullable = true)
        String updatedBy,
        Integer quantity,
        Long variantId,
        String variantName,
        Integer variantQuantity,
        String sku,
        BigDecimal variantPrice,
        @Nullable
        BigDecimal variantPromotionalPrice,
        BigDecimal totalPrice,
        @Nullable
        BigDecimal totalPromotionalPrice,
        String productSlug,
        String productUnit,
        String productName,
        String productImage,
        Boolean isPromotion
) {

}
