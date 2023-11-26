package vn.com.greencraze.auth.dto.request.auth;

import jakarta.validation.constraints.NotBlank;

public record ForgotPasswordRequest(
        @NotBlank
        String email
) {}
