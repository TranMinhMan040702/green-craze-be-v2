package vn.com.greencraze.product.dto.request.unit;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateUnitRequest(
        @NotBlank
        String name,
        @NotNull
        Boolean status
) {}
