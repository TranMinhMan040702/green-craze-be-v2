package vn.com.greencraze.auth.dto.response.identity;

import lombok.Builder;

@Builder
public record AuthenticateResponse(
        String accessToken,
        String refreshToken
) {}
