package vn.com.greencraze.order.client.user;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import vn.com.greencraze.order.client.user.dto.request.UpdateUserCartRequest;
import vn.com.greencraze.order.client.user.dto.response.GetOneUserResponse;

@FeignClient("user-service")
public interface UserServiceClient {
    
    String BASE = "/core/user";

    @GetMapping(BASE + "/users/{id}")
    GetOneUserResponse getOneUser(@PathVariable String id);

    @PutMapping(BASE + "/carts/update-user-cart")
    void updateUserCart(@RequestBody UpdateUserCartRequest request);

}
