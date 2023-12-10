package vn.com.greencraze.meta.dto.response;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record StatisticRevenueExpenseResponse(
        String date,
        BigDecimal revenue,
        BigDecimal expense
) {}
