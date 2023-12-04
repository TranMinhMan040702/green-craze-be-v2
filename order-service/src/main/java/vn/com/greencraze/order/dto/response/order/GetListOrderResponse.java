package vn.com.greencraze.order.dto.response.order;

import lombok.With;
import vn.com.greencraze.order.client.address.dto.response.GetOneAddressResponse;
import vn.com.greencraze.order.client.user.dto.response.GetOneUserResponse;
import vn.com.greencraze.order.dto.response.orderCancelReason.GetOneOrderCancelReasonResponse;
import vn.com.greencraze.order.dto.response.transaction.GetOneTransactionResponse;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@With
public record GetListOrderResponse(
        Long id,
        Instant createdAt,
        Instant updatedAt,
        String createdBy,
        String updatedBy,
        String userId,
        Long addressId,
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
        GetOneUserResponse user,
        GetOneAddressResponse address,
        GetOneTransactionResponse transaction,
        GetOneOrderCancelReasonResponse cancelReason,
        List<GetListOrderItemResponse> items
) {
}
