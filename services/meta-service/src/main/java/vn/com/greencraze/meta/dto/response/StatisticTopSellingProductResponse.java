package vn.com.greencraze.meta.dto.response;

import lombok.Builder;

@Builder
public record StatisticTopSellingProductResponse(
        String name,
        Long value
) {}
