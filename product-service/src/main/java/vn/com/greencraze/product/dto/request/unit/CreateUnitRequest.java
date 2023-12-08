package vn.com.greencraze.product.dto.request.unit;

import jakarta.validation.constraints.NotBlank;

public record CreateUnitRequest(
        @NotBlank
        String name
) {}
