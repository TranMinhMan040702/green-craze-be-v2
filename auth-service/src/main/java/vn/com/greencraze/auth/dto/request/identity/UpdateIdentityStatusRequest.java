package vn.com.greencraze.auth.dto.request.identity;

import vn.com.greencraze.auth.enumeration.IdentityStatus;

public record UpdateIdentityStatusRequest(
        String id,
        IdentityStatus status
) {}
