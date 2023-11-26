package vn.com.greencraze.auth.dto.response.auth;

import lombok.Builder;

@Builder
public record RefreshTokenResponse(
        String accessToken,
        String refreshToken
) {}
