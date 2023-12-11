package vn.com.greencraze.meta.dto.response;

import lombok.Builder;

@Builder
public record StatisticOrderStatusResponse(
        String name,
        Long value
) {}
