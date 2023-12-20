package vn.com.greencraze.meta.dto.response;

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
