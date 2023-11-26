package vn.com.greencraze.auth.client.user.dto;

import lombok.Builder;

@Builder
public record CreateUserRequest(
        String identityId,
        String email,
        String firstName,
        String lastName
) {}
