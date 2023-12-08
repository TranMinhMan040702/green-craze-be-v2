package vn.com.greencraze.auth.dto.request.identity;

import jakarta.validation.constraints.NotBlank;

public record EnableOneIdentityRequest(
        @NotBlank
        String id
) {}
