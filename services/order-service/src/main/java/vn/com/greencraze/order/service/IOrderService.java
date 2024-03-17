package vn.com.greencraze.order.service;

import vn.com.greencraze.commons.api.ListResponse;
import vn.com.greencraze.commons.api.RestResponse;
import vn.com.greencraze.commons.enumeration.OrderStatus;
import vn.com.greencraze.order.dto.request.order.CompletePaypalOrderRequest;
import vn.com.greencraze.order.dto.request.order.CreateOrderRequest;
import vn.com.greencraze.order.dto.request.order.UpdateOrderRequest;
import vn.com.greencraze.order.dto.response.order.CreateOrderResponse;
import vn.com.greencraze.order.dto.response.order.GetListOrderResponse;
import vn.com.greencraze.order.dto.response.order.GetOneOrderItemResponse;
import vn.com.greencraze.order.dto.response.order.GetOneOrderResponse;
import vn.com.greencraze.order.dto.response.order.GetTop5OrderLatestResponse;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public interface IOrderService {

    RestResponse<ListResponse<GetListOrderResponse>> getListOrder(
            Integer page, Integer size, Boolean isSortAscending, String columnName,
            String search, Boolean all, OrderStatus status);

    RestResponse<ListResponse<GetListOrderResponse>> getListUserOrder(
            Integer page, Integer size, Boolean isSortAscending, String columnName,
            String search, Boolean all, OrderStatus status);

    List<GetTop5OrderLatestResponse> getTop5OrderLatest();

    RestResponse<GetOneOrderResponse> getOneOrder(Long id);

    RestResponse<GetOneOrderResponse> getOneOrderByCode(String code);

    RestResponse<CreateOrderResponse> createOrder(CreateOrderRequest request);

    void updateOrder(Long id, UpdateOrderRequest request);

    void completePaypalOrder(Long id, CompletePaypalOrderRequest request);

    // call from other service
    RestResponse<GetOneOrderItemResponse> getOneOrderItem(Long orderItemId);

    Long getTotalOrderWithStatusDelivered();

    Map<String, Long> getTopSellingProduct(Instant startDate, Instant endDate);

    Map<String, Long> getOrderTotalByStatus(Instant startDate, Instant endDate);

    void cancelOrderSaga(Long id);

}
