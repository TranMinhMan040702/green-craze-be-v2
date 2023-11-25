package vn.com.greencraze.auth.dto.request.identity;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenRequest(
        @NotBlank
        String accessToken,
        @NotBlank
        String refreshToken
) {}
