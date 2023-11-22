package vn.com.greencraze.order.mapper;

import org.mapstruct.Mapper;
import vn.com.greencraze.commons.mapper.ReferenceMapper;
import vn.com.greencraze.order.dto.response.order.GetListOrderItemResponse;
import vn.com.greencraze.order.entity.OrderItem;

import java.util.List;
import java.util.Set;

@Mapper(uses = {ReferenceMapper.class})
public interface OrderItemMapper {
    List<GetListOrderItemResponse> setOrderItemToListGetListOrderItemResponse(Set<OrderItem> orderItems);
}
