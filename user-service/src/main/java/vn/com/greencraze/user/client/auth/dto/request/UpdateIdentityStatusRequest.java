package vn.com.greencraze.user.client.auth.dto.request;


import lombok.Builder;
import vn.com.greencraze.user.enumeration.IdentityStatus;

@Builder
public record UpdateIdentityStatusRequest(
        String id,
        IdentityStatus status
) {}
