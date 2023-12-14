package vn.com.greencraze.auth.dto.request.identity;

import jakarta.validation.constraints.NotBlank;

public record CreateIdentityRequest(
        @NotBlank
        String email,
        @NotBlank
        String password
) {}
