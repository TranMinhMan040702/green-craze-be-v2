package vn.com.greencraze.order.service;

import vn.com.greencraze.commons.api.ListResponse;
import vn.com.greencraze.commons.api.RestResponse;
import vn.com.greencraze.order.dto.request.order.CompletePaypalOrderRequest;
import vn.com.greencraze.order.dto.request.order.CreateOrderRequest;
import vn.com.greencraze.order.dto.request.order.UpdateOrderRequest;
import vn.com.greencraze.order.dto.response.order.CreateOrderResponse;
import vn.com.greencraze.order.dto.response.order.GetListOrderResponse;
import vn.com.greencraze.order.dto.response.order.GetOneOrderItemResponse;
import vn.com.greencraze.order.dto.response.order.GetOneOrderResponse;
import vn.com.greencraze.order.enumeration.OrderStatus;

import java.util.List;

public interface IOrderService {

    RestResponse<ListResponse<GetListOrderResponse>> getListOrder(
            Integer page, Integer size, Boolean isSortAscending, String columnName,
            String search, Boolean all, OrderStatus status);

    RestResponse<ListResponse<GetListOrderResponse>> getListUserOrder(
            Integer page, Integer size, Boolean isSortAscending, String columnName,
            String search, Boolean all, OrderStatus status);

    RestResponse<List<GetListOrderResponse>> getTop5OrderLatest();

    RestResponse<GetOneOrderResponse> getOneOrder(Long id);

    RestResponse<GetOneOrderResponse> getOneOrderByCode(String code);

    RestResponse<CreateOrderResponse> createOrder(CreateOrderRequest request);

    void updateOrder(Long id, UpdateOrderRequest request);

    void completePaypalOrder(Long id, CompletePaypalOrderRequest request);

    // call from other service
    RestResponse<GetOneOrderItemResponse> getOneOrderItem(Long orderItemId);

}
