package vn.com.greencraze.auth.dto.request.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import vn.com.greencraze.auth.enumeration.TokenType;

@Builder
public record VerifyOTPRequest(
        @NotBlank
        String email,
        @NotBlank
        String otp,
        @NotNull
        TokenType type
) {}
