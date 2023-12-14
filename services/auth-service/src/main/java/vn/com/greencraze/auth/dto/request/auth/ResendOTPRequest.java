package vn.com.greencraze.auth.dto.request.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import vn.com.greencraze.auth.enumeration.TokenType;

@Builder
public record ResendOTPRequest(
        @NotBlank
        String email,
        @NotNull
        TokenType type
) {}
