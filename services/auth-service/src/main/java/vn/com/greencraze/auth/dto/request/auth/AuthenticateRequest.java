package vn.com.greencraze.auth.dto.request.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record AuthenticateRequest(
        @NotBlank
        String username,
        @NotBlank
        String password
) {}
