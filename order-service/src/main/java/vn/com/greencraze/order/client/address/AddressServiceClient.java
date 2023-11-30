package vn.com.greencraze.order.client.address;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import vn.com.greencraze.order.client.address.dto.response.GetOneAddressResponse;

@FeignClient("address-service")
public interface AddressServiceClient {

    String BASE = "/core/address";

    @GetMapping(BASE + "/addresses/{id}")
    GetOneAddressResponse getOneAddress(@PathVariable Long id);

    @GetMapping(BASE + "/addresses/default/{userId}")
    GetOneAddressResponse getDefaultAddress(@PathVariable String userId);

}
