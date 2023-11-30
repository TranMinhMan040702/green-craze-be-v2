package vn.com.greencraze.order.dto.response.order;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.lang.Nullable;
import vn.com.greencraze.order.client.address.dto.response.GetOneAddressResponse;
import vn.com.greencraze.order.client.user.dto.response.GetOneUserResponse;
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
        List<GetListOrderItemResponse> items) {
    public GetListOrderResponse setValues(GetOneUserResponse user, GetOneAddressResponse address, List<GetListOrderItemResponse> items) {
        return new GetListOrderResponse(id(), createdAt(), updatedAt(), createdBy(), updatedBy(), userId(), addressId(), otherCancelReason(),
                totalAmount(), tax(), shippingCost(), paymentStatus(), note(), status(), code(), deliveryMethod(),
                isReview(), reviewedDate(), user, address, transaction(), cancelReason(), items);
    }
}
