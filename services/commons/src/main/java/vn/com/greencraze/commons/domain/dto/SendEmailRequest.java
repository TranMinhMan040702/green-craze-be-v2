package vn.com.greencraze.commons.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import vn.com.greencraze.commons.enumeration.EmailEvent;

import java.util.Map;

@Builder
public record SendEmailRequest(
        @NotNull
        EmailEvent event,
        @NotBlank
        String email,
        @NotNull(message = "request has no payload")
        Map<String, Object> payload
) {}
