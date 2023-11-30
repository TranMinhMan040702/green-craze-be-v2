package vn.com.greencraze.order.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import vn.com.greencraze.commons.mapper.ReferenceMapper;
import vn.com.greencraze.order.dto.request.delivery.CreateDeliveryRequest;
import vn.com.greencraze.order.dto.request.delivery.UpdateDeliveryRequest;
import vn.com.greencraze.order.dto.response.delivery.CreateDeliveryResponse;
import vn.com.greencraze.order.dto.response.delivery.GetListDeliveryResponse;
import vn.com.greencraze.order.dto.response.delivery.GetOneDeliveryResponse;
import vn.com.greencraze.order.entity.Delivery;

@Mapper(uses = {ReferenceMapper.class})
public interface DeliveryMapper {

    @Mapping(target = "id", ignore = true)
    Delivery idToDelivery(String id);

    GetListDeliveryResponse deliveryToGetListDeliveryResponse(Delivery delivery);

    GetOneDeliveryResponse deliveryToGetOneDeliveryResponse(Delivery delivery);

    @Mapping(target = "image", ignore = true)
    Delivery createDeliveryRequestToDelivery(CreateDeliveryRequest createDeliveryRequest);

    CreateDeliveryResponse deliveryToCreateDeliveryResponse(Delivery delivery);

    @Mapping(target = "image", ignore = true)
    Delivery updateDeliveryFromUpdateDeliveryRequest(
            @MappingTarget Delivery delivery, UpdateDeliveryRequest updateDeliveryRequest);

}
