package vn.com.greencraze.product.dto.request.product;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

@Builder
public record FilterProductRequest(
        BigDecimal minPrice,
        BigDecimal maxPrice,
        List<Long> brandIds,
        List<Long> categoryIds,
        String categorySlug,
        Integer rating,
        String columnName,
        Boolean isSortAscending
) {}
