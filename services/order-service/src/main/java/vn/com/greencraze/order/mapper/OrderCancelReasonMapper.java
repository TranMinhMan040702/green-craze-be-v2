package vn.com.greencraze.order.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import vn.com.greencraze.commons.mapper.ReferenceMapper;
import vn.com.greencraze.order.dto.request.orderCancelReason.CreateOrderCancelReasonRequest;
import vn.com.greencraze.order.dto.request.orderCancelReason.UpdateOrderCancelReasonRequest;
import vn.com.greencraze.order.dto.response.orderCancelReason.CreateOrderCancelReasonResponse;
import vn.com.greencraze.order.dto.response.orderCancelReason.GetListOrderCancelReasonResponse;
import vn.com.greencraze.order.dto.response.orderCancelReason.GetOneOrderCancelReasonResponse;
import vn.com.greencraze.order.entity.OrderCancelReason;

@Mapper(uses = {ReferenceMapper.class})
public interface OrderCancelReasonMapper {

    @Mapping(target = "id", ignore = true)
    OrderCancelReason idToOrderCancelReason(String id);

    GetListOrderCancelReasonResponse orderCancelReasonToGetListOrderCancelReasonResponse(
            OrderCancelReason orderCancelReason);

    GetOneOrderCancelReasonResponse orderCancelReasonToGetOneOrderCancelReasonResponse(
            OrderCancelReason orderCancelReason);

    OrderCancelReason createOrderCancelReasonRequestToOrderCancelReason(
            CreateOrderCancelReasonRequest createOrderCancelReasonRequest);

    CreateOrderCancelReasonResponse orderCancelReasonToCreateOrderCancelReasonResponse(
            OrderCancelReason orderCancelReason);

    OrderCancelReason updateOrderCancelReasonFromUpdateOrderCancelReasonRequest(
            @MappingTarget OrderCancelReason orderCancelReason,
            UpdateOrderCancelReasonRequest updateOrderCancelReasonRequest);

}
