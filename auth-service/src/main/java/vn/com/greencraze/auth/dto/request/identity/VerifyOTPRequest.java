package vn.com.greencraze.auth.dto.request.identity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import vn.com.greencraze.auth.enumeration.TokenType;

@Builder
public record VerifyOTPRequest(
        @NotBlank
        String email,
        @NotBlank
        String OTP,
        @NotNull
        TokenType type
) {}
