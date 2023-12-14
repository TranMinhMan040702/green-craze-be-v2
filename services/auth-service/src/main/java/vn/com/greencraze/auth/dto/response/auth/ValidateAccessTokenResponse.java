package vn.com.greencraze.auth.dto.response.auth;

import lombok.Builder;

import java.util.List;

@Builder
public record ValidateAccessTokenResponse(
        String userId,
        List<String> userAuthorities
) {}
