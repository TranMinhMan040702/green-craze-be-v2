package vn.com.greencraze.order.service;

import vn.com.greencraze.commons.api.ListResponse;
import vn.com.greencraze.commons.api.RestResponse;
import vn.com.greencraze.order.dto.request.paymentMethod.CreatePaymentMethodRequest;
import vn.com.greencraze.order.dto.request.paymentMethod.UpdatePaymentMethodRequest;
import vn.com.greencraze.order.dto.response.paymentMethod.CreatePaymentMethodResponse;
import vn.com.greencraze.order.dto.response.paymentMethod.GetListPaymentMethodResponse;
import vn.com.greencraze.order.dto.response.paymentMethod.GetOnePaymentMethodResponse;

import java.util.List;

public interface IPaymentMethodService {

    RestResponse<ListResponse<GetListPaymentMethodResponse>> getListPaymentMethod(
            Integer page, Integer size, Boolean isSortAscending,
            String columnName, String search, Boolean all, Boolean status);

    RestResponse<GetOnePaymentMethodResponse> getOnePaymentMethod(Long id);

    RestResponse<CreatePaymentMethodResponse> createPaymentMethod(CreatePaymentMethodRequest request);

    void updatePaymentMethod(Long id, UpdatePaymentMethodRequest request);

    void deleteOnePaymentMethod(Long id);

    void deleteListPaymentMethod(List<Long> ids);

}
