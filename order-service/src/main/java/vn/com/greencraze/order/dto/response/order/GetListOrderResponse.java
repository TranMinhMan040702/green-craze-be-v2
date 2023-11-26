package vn.com.greencraze.order.dto.response.order;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.lang.Nullable;
import vn.com.greencraze.order.dto.response.orderCancelReason.GetOneOrderCancelReasonResponse;
import vn.com.greencraze.order.dto.response.transaction.GetOneTransactionResponse;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record GetListOrderResponse(
        Long id,
        Instant createdAt,
        Instant updatedAt,
        @Nullable
        @Schema(nullable = true)
        String createdBy,
        @Nullable
        @Schema(nullable = true)
        String updatedBy,
        String otherCancelReason,
        BigDecimal totalAmount,
        Double tax,
        BigDecimal shippingCost,
        Boolean paymentStatus,
        String note,
        String status,
        String code,
        String deliveryMethod,
        Boolean isReview,
        Instant reviewedDate,
        //UserDto user,
        //AddressDto address,
        GetOneTransactionResponse transaction,
        GetOneOrderCancelReasonResponse cancelReason,
        List<GetListOrderItemResponse> items
) {
}
