package vn.com.greencraze.meta.dto.response;

import lombok.Builder;

@Builder
public record StatisticReviewResponse(
        String name,
        Long value
) {}
