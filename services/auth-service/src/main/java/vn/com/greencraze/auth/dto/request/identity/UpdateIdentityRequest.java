package vn.com.greencraze.auth.dto.request.identity;

import jakarta.validation.constraints.NotBlank;

public record UpdateIdentityRequest(
        @NotBlank
        String id,
        @NotBlank
        String password
) {}
