package vn.com.greencraze.auth.dto.request.identity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import vn.com.greencraze.auth.enumeration.TokenType;

public record ResetPasswordRequest(
        @NotBlank
        String email,
        @NotBlank
        String password,
        @NotBlank
        String OTP,
        @NotNull
        TokenType type
) {}
