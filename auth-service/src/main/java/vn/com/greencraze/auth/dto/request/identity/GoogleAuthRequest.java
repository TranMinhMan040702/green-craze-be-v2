package vn.com.greencraze.auth.dto.request.identity;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record GoogleAuthRequest(
        @NotBlank
        String googleToken
) {}
