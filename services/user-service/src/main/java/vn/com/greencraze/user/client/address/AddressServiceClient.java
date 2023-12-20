package vn.com.greencraze.user.client.address;

import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import vn.com.greencraze.commons.api.RestResponse;
import vn.com.greencraze.user.client.address.dto.request.CreateStaffAddressRequest;
import vn.com.greencraze.user.client.address.dto.request.UpdateStaffAddressRequest;
import vn.com.greencraze.user.client.address.dto.response.GetListAddressByUserIdResponse;

import java.util.List;

@FeignClient("address-service")
public interface AddressServiceClient {

    String BASE = "/core/address";

    @GetMapping(BASE + "/addresses/user/{id}")
    RestResponse<List<GetListAddressByUserIdResponse>> getListAddressByUserId(@PathVariable String id);

    @PostMapping(BASE + "/addresses/staff")
    void createStaffAddress(@RequestBody @Valid CreateStaffAddressRequest request);

    @PutMapping(BASE + "/addresses/staff/{id}")
    void updateStaffAddress(@PathVariable Long id, @RequestBody @Valid UpdateStaffAddressRequest request);

}
