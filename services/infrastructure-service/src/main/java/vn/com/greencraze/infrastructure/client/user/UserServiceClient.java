package vn.com.greencraze.infrastructure.client.user;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient("user-service")
public interface UserServiceClient {

    String BASE = "/core/user";

    @GetMapping(BASE + "/users/get-all-user-id")
    List<String> getAllUserId();

    @GetMapping(BASE + "/users/get-username/{userId}")
    String getUsername(@PathVariable String userId);

}
