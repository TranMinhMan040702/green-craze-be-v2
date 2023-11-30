package vn.com.greencraze.gateway.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record ValidateAccessTokenResponse(
        String userId,
        List<String> userAuthorities
) {}
