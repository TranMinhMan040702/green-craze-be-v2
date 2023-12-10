package vn.com.greencraze.order.dto.response.transaction;

import lombok.Builder;
import lombok.With;

import java.math.BigDecimal;
import java.time.Instant;

@Builder
@With
public record GetTop5TransactionLatestResponse(
        Long id,
        Instant createdAt,
        BigDecimal totalPay,
        UserResponse user
) {

    @Builder
    public record UserResponse(
            String firstName,
            String lastName,
            String avatar
    ) {}

}
