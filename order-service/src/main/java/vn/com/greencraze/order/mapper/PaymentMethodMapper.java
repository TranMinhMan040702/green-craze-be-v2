package vn.com.greencraze.order.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import vn.com.greencraze.commons.mapper.ReferenceMapper;
import vn.com.greencraze.order.dto.request.paymentMethod.CreatePaymentMethodRequest;
import vn.com.greencraze.order.dto.request.paymentMethod.UpdatePaymentMethodRequest;
import vn.com.greencraze.order.dto.response.paymentMethod.CreatePaymentMethodResponse;
import vn.com.greencraze.order.dto.response.paymentMethod.GetListPaymentMethodResponse;
import vn.com.greencraze.order.dto.response.paymentMethod.GetOnePaymentMethodResponse;
import vn.com.greencraze.order.entity.PaymentMethod;

@Mapper(uses = {ReferenceMapper.class})
public interface PaymentMethodMapper {

    @Mapping(target = "id", ignore = true)
    PaymentMethod idToPaymentMethod(String id);

    GetListPaymentMethodResponse paymentMethodToGetListPaymentMethodResponse(PaymentMethod paymentMethod);

    GetOnePaymentMethodResponse paymentMethodToGetOnePaymentMethodResponse(PaymentMethod paymentMethod);

    @Mapping(target = "image", ignore = true)
    PaymentMethod createPaymentMethodRequestToPaymentMethod(CreatePaymentMethodRequest createPaymentMethodRequest);

    CreatePaymentMethodResponse paymentMethodToCreatePaymentMethodResponse(PaymentMethod paymentMethod);

    @Mapping(target = "image", ignore = true)
    PaymentMethod updatePaymentMethodFromUpdatePaymentMethodRequest(@MappingTarget PaymentMethod paymentMethod, UpdatePaymentMethodRequest updatePaymentMethodRequest);

}
