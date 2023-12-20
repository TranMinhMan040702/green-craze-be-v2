package vn.com.greencraze.user.client.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record UpdateIdentityRequest(
        @NotBlank
        String id,
        @NotBlank
        String password
) {}
