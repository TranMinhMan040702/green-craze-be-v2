package vn.com.greencraze.user.dto.request.user;

import jakarta.validation.constraints.NotBlank;

public record CreateUserRequest(
        @NotBlank
        String identityId,
        @NotBlank
        String email,
        @NotBlank
        String firstName,
        @NotBlank
        String lastName
) {}
