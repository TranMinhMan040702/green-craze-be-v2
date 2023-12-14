package vn.com.greencraze.meta.dto.response;

import lombok.Builder;

import java.util.Map;

@Builder
public record StatisticTopSellingProductYearResponse(
        String date,
        Map<String, Long> products
) {}
