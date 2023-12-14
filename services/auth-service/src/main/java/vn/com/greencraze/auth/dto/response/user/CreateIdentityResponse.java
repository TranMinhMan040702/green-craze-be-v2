package vn.com.greencraze.auth.dto.response.user;

import lombok.Builder;

@Builder
public record CreateIdentityResponse(
        String id
) {}
