package vn.com.greencraze.auth.dto.request.identity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import vn.com.greencraze.auth.enumeration.TokenType;

public record ResendOTPRequest(
        @NotBlank
        String email,
        @NotNull
        TokenType type
) {}
