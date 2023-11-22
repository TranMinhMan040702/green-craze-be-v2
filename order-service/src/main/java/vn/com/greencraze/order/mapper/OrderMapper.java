package vn.com.greencraze.order.mapper;

import org.mapstruct.Mapper;
import vn.com.greencraze.commons.mapper.ReferenceMapper;
import vn.com.greencraze.order.dto.response.order.GetListOrderResponse;
import vn.com.greencraze.order.dto.response.order.GetOneOrderResponse;
import vn.com.greencraze.order.entity.Order;

@Mapper(uses = {ReferenceMapper.class})
public interface OrderMapper {
    GetListOrderResponse orderToGetListOrderResponse(Order order);

    GetOneOrderResponse orderToGetOneOrderResponse(Order order);
}
