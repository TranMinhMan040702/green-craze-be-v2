package vn.com.greencraze.auth.client.user;

import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import vn.com.greencraze.auth.client.user.dto.CreateUserRequest;

@FeignClient("user-service")
public interface UserServiceClient {

    String BASE = "/core/user";

    @PostMapping(BASE + "/users")
    void createUser(@RequestBody @Valid CreateUserRequest request);

}
