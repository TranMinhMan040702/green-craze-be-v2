package vn.com.greencraze.product.dto.response.sale;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.lang.Nullable;
import vn.com.greencraze.product.enumeration.SaleStatus;

import java.time.Instant;
import java.util.List;

public record CreateSaleResponse(
        Long id,
        Instant createdAt,
        Instant updatedAt,
        @Nullable
        @Schema(nullable = true)
        String createdBy,
        @Nullable
        @Schema(nullable = true)
        String updatedBy,
        String name,
        String description,
        String image,
        Instant startDate,
        Instant endDate,
        Double promotionalPercent,
        String slug,
        SaleStatus status,
        Boolean allProductCategory,
        List<ProductCategoryResponse> productCategories
) {

    public record ProductCategoryResponse(
            String name,
            String parentName,
            String image
    ) {}

}
