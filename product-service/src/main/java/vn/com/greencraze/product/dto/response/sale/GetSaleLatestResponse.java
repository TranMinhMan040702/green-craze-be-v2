package vn.com.greencraze.product.dto.response.sale;

import lombok.Builder;

import java.time.Instant;

@Builder
public record GetSaleLatestResponse(
        Long id,
        String name,
        String description,
        String image,
        Instant startDate,
        Instant endDate,
        Double promotionalPercent
) {}
