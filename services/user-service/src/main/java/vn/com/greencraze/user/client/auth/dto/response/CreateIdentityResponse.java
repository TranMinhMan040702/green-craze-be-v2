package vn.com.greencraze.user.client.auth.dto.response;

import lombok.Builder;

@Builder
public record CreateIdentityResponse(
        String id
) {}
