package vn.com.greencraze.auth.dto.request.auth;

import jakarta.validation.constraints.NotBlank;

public record RegisterRequest(
        @NotBlank
        String email,
        @NotBlank
        String password,
        @NotBlank
        String firstName,
        @NotBlank
        String lastName
) {}
