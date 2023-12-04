package vn.com.greencraze.product.dto.request.product;

import java.math.BigDecimal;
import java.util.List;

public record FilterProductRequest(
        BigDecimal minPrice,
        BigDecimal maxPrice,
        List<Long> brandIds,
        List<Long> categoryIds,
        String categorySlug,
        Integer rating
) {}
