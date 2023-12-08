package vn.com.greencraze.auth.dto.request.identity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import vn.com.greencraze.auth.enumeration.IdentityStatus;

public record UpdateIdentityStatusRequest(
        @NotBlank
        String id,
        @NotNull
        IdentityStatus status
) {}
