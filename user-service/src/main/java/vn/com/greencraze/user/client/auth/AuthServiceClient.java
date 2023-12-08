package vn.com.greencraze.user.client.auth;

import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import vn.com.greencraze.commons.api.RestResponse;
import vn.com.greencraze.user.client.auth.dto.request.CreateIdentityRequest;
import vn.com.greencraze.user.client.auth.dto.request.DisableListIdentityRequest;
import vn.com.greencraze.user.client.auth.dto.request.UpdateIdentityRequest;
import vn.com.greencraze.user.client.auth.dto.request.UpdateIdentityStatusRequest;
import vn.com.greencraze.user.client.auth.dto.response.CreateIdentityResponse;

@FeignClient("auth-service")
public interface AuthServiceClient {

    String BASE = "/core/auth";

    @PutMapping(BASE + "/identities/update-status")
    void updateIdentityStatus(@RequestBody @Valid UpdateIdentityStatusRequest request);

    @PutMapping(BASE + "/identities/disable")
    void disableListIdentity(@RequestBody @Valid DisableListIdentityRequest request);

    @PostMapping(BASE + "/identities")
    RestResponse<CreateIdentityResponse> createIdentity(@RequestBody @Valid CreateIdentityRequest request);

    @PutMapping(BASE + "/identities/update-identity")
    void updateIdentity(@RequestBody @Valid UpdateIdentityRequest request);

}
