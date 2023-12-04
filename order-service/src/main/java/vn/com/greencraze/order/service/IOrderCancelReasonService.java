package vn.com.greencraze.order.service;

import vn.com.greencraze.commons.api.ListResponse;
import vn.com.greencraze.commons.api.RestResponse;
import vn.com.greencraze.order.dto.request.orderCancelReason.CreateOrderCancelReasonRequest;
import vn.com.greencraze.order.dto.request.orderCancelReason.UpdateOrderCancelReasonRequest;
import vn.com.greencraze.order.dto.response.orderCancelReason.CreateOrderCancelReasonResponse;
import vn.com.greencraze.order.dto.response.orderCancelReason.GetListOrderCancelReasonResponse;
import vn.com.greencraze.order.dto.response.orderCancelReason.GetOneOrderCancelReasonResponse;

import java.util.List;

public interface IOrderCancelReasonService {

    RestResponse<ListResponse<GetListOrderCancelReasonResponse>> getListOrderCancelReason(
            Integer page, Integer size, Boolean isSortAscending,
            String columnName, String search, Boolean all, Boolean status);

    RestResponse<GetOneOrderCancelReasonResponse> getOneOrderCancelReason(Long id);

    RestResponse<CreateOrderCancelReasonResponse> createOrderCancelReason(CreateOrderCancelReasonRequest request);

    void updateOrderCancelReason(Long id, UpdateOrderCancelReasonRequest request);

    void deleteOneOrderCancelReason(Long id);

    void deleteListOrderCancelReason(List<Long> ids);

}
