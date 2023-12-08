package vn.com.greencraze.order.dto.response.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.With;
import org.springframework.lang.Nullable;
import vn.com.greencraze.order.client.address.dto.response.GetOneAddressResponse;
import vn.com.greencraze.order.client.user.dto.response.GetOneUserResponse;
import vn.com.greencraze.order.dto.response.orderCancelReason.GetOneOrderCancelReasonResponse;
import vn.com.greencraze.order.dto.response.transaction.GetOneTransactionResponse;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@With
public record GetOneOrderResponse(
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
        @Nullable
        Instant reviewedDate,
        GetOneUserResponse user,
        GetOneAddressResponse address,
        GetOneTransactionResponse transaction,
        GetOneOrderCancelReasonResponse cancelReason,
        List<GetListOrderItemResponse> items
) {
}
