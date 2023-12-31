package vn.com.greencraze.order.mapper;

import org.mapstruct.Mapper;
import vn.com.greencraze.commons.mapper.ReferenceMapper;
import vn.com.greencraze.order.dto.response.order.CreateOrderResponse;
import vn.com.greencraze.order.dto.response.order.GetListOrderResponse;
import vn.com.greencraze.order.dto.response.order.GetOneOrderResponse;
import vn.com.greencraze.order.dto.response.order.GetTop5OrderLatestResponse;
import vn.com.greencraze.order.entity.Order;

@Mapper(uses = {ReferenceMapper.class, OrderItemMapper.class, TransactionMapper.class, OrderCancelReasonMapper.class})
public interface OrderMapper {

    GetListOrderResponse orderToGetListOrderResponse(Order order);

    GetOneOrderResponse orderToGetOneOrderResponse(Order order);

    CreateOrderResponse orderToCreateOrderResponse(Order order);

    GetTop5OrderLatestResponse orderToGetTop5OrderLatestResponse(Order order);

}
