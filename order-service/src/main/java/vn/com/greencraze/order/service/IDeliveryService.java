package vn.com.greencraze.order.service;

import vn.com.greencraze.commons.api.ListResponse;
import vn.com.greencraze.commons.api.RestResponse;
import vn.com.greencraze.order.dto.request.delivery.CreateDeliveryRequest;
import vn.com.greencraze.order.dto.request.delivery.UpdateDeliveryRequest;
import vn.com.greencraze.order.dto.response.delivery.CreateDeliveryResponse;
import vn.com.greencraze.order.dto.response.delivery.GetListDeliveryResponse;
import vn.com.greencraze.order.dto.response.delivery.GetOneDeliveryResponse;

import java.util.List;

public interface IDeliveryService {

    RestResponse<ListResponse<GetListDeliveryResponse>> getListDelivery(
            Integer page, Integer size, Boolean isSortAscending, String columnName, String search, Boolean all
    );

    RestResponse<GetOneDeliveryResponse> getOneDelivery(Long id);

    RestResponse<CreateDeliveryResponse> createDelivery(CreateDeliveryRequest request);

    void updateDelivery(Long id, UpdateDeliveryRequest request);

    void deleteOneDelivery(Long id);

    void deleteListDelivery(List<Long> ids);

}
