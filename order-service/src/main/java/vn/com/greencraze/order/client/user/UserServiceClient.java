package vn.com.greencraze.order.client.user;

import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import vn.com.greencraze.commons.api.RestResponse;
import vn.com.greencraze.order.client.user.dto.request.UpdateUserCartRequest;
import vn.com.greencraze.order.client.user.dto.response.GetOneUserResponse;
import vn.com.greencraze.order.client.user.dto.response.GetOrderReviewResponse;

import java.util.List;

@FeignClient("user-service")
public interface UserServiceClient {

    String BASE = "/core/user";

    @GetMapping(BASE + "/users/other/{id}")
    RestResponse<GetOneUserResponse> getOneUser(@PathVariable String id);

    @GetMapping(BASE + "/reviews/order-review")
    RestResponse<GetOrderReviewResponse> getOrderReview(@RequestParam List<Long> orderItemIds);

    @PutMapping(BASE + "/carts/update-user-cart")
    void updateUserCart(@RequestBody @Valid UpdateUserCartRequest request);

}
