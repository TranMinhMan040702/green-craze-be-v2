package vn.com.greencraze.auth.dto.response.identity;

import lombok.Builder;

@Builder
public record RefreshTokenResponse(
        String accessToken,
        String refreshToken
) {}
