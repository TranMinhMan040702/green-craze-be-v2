package vn.com.greencraze.auth.dto.request.auth;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenRequest(
        @NotBlank
        String accessToken,
        @NotBlank
        String refreshToken
) {}
