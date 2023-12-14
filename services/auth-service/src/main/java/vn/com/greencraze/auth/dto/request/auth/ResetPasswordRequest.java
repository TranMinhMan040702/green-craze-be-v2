package vn.com.greencraze.auth.dto.request.auth;

import jakarta.validation.constraints.NotBlank;

public record ResetPasswordRequest(
        @NotBlank
        String email,
        @NotBlank
        String password,
        @NotBlank
        String otp
) {}
