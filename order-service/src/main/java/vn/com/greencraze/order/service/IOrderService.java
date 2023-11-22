package vn.com.greencraze.order.service;

import vn.com.greencraze.commons.api.ListResponse;
import vn.com.greencraze.commons.api.RestResponse;
import vn.com.greencraze.order.dto.request.order.CompletePaypalOrderRequest;
import vn.com.greencraze.order.dto.request.order.CreateOrderRequest;
import vn.com.greencraze.order.dto.request.order.UpdateOrderRequest;
import vn.com.greencraze.order.dto.response.order.CreateOrderResponse;
import vn.com.greencraze.order.dto.response.order.GetListOrderResponse;
import vn.com.greencraze.order.dto.response.order.GetOneOrderResponse;

import java.util.List;

public interface IOrderService {
    RestResponse<ListResponse<GetListOrderResponse>> getListOrder(
            Integer page, Integer size, Boolean isSortAscending, String columnName, String search, Boolean all, String status
    );

    RestResponse<ListResponse<GetListOrderResponse>> getListUserOrder(
            Integer page, Integer size, Boolean isSortAscending, String columnName, String search, Boolean all, String status
    );

    RestResponse<List<GetListOrderResponse>> getTop5OrderLastest();

    RestResponse<GetOneOrderResponse> getOneOrder(Long id);

    RestResponse<GetOneOrderResponse> getOneOrderByCode(String code);

    RestResponse<CreateOrderResponse> createOrder(CreateOrderRequest request);

    void updateOrder(UpdateOrderRequest request);

    void completePaypalOrder(CompletePaypalOrderRequest request);
}
