package vn.com.greencraze.user.client.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record CreateIdentityRequest(
        @NotBlank
        String email,
        @NotBlank
        String password
) {}
