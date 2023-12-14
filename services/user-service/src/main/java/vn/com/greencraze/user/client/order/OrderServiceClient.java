package vn.com.greencraze.user.client.order;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import vn.com.greencraze.commons.api.RestResponse;
import vn.com.greencraze.user.client.order.dto.response.GetOneOrderItemResponse;

@FeignClient("order-service")
public interface OrderServiceClient {

    String BASE = "/core/order";

    @GetMapping(BASE + "/orders/order-items/{orderItemId}")
    RestResponse<GetOneOrderItemResponse> GetOneOrderItem(@PathVariable Long orderItemId);

}
