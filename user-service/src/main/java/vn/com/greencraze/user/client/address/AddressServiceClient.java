package vn.com.greencraze.user.client.address;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import vn.com.greencraze.commons.api.RestResponse;
import vn.com.greencraze.user.client.address.dto.response.GetListAddressByUserIdResponse;

import java.util.List;

@FeignClient("address-service")
public interface AddressServiceClient {

    String BASE = "/core/address";

    @GetMapping(BASE + "/addresses/user/{id}")
    RestResponse<List<GetListAddressByUserIdResponse>> getListAddressByUserId(@PathVariable String id);

}
