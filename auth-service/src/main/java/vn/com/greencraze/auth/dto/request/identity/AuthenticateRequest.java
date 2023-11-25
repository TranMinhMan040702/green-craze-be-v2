package vn.com.greencraze.auth.dto.request.identity;

import jakarta.validation.constraints.NotBlank;

public record AuthenticateRequest(
        @NotBlank
        String username,
        @NotBlank
        String password
) {}
