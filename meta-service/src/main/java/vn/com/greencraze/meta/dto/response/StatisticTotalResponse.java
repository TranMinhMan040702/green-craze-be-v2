package vn.com.greencraze.meta.dto.response;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record StatisticTotalResponse(
        BigDecimal revenue,
        BigDecimal expense,
        Long user,
        Long order
) {}
