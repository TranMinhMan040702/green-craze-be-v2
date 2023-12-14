package vn.com.greencraze.auth.service;

import vn.com.greencraze.auth.dto.request.identity.ChangePasswordRequest;
import vn.com.greencraze.auth.dto.request.identity.CreateIdentityRequest;
import vn.com.greencraze.auth.dto.request.identity.DisableListIdentityRequest;
import vn.com.greencraze.auth.dto.request.identity.UpdateIdentityRequest;
import vn.com.greencraze.auth.dto.request.identity.UpdateIdentityStatusRequest;
import vn.com.greencraze.auth.dto.response.user.CreateIdentityResponse;
import vn.com.greencraze.commons.api.RestResponse;

public interface IIdentityService {

    void updateIdentityStatus(UpdateIdentityStatusRequest request);

    void disableListIdentity(DisableListIdentityRequest request);

    RestResponse<CreateIdentityResponse> createIdentity(CreateIdentityRequest request);

    void updateIdentity(UpdateIdentityRequest request);

    void changePassword(ChangePasswordRequest request);

}
