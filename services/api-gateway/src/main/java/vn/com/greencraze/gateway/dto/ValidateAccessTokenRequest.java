package vn.com.greencraze.gateway.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record ValidateAccessTokenRequest(@NotBlank String accessToken) {}
