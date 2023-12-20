package vn.com.greencraze.order.client.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record UpdateUserCartRequest(
        @NotBlank
        String userId,
        @NotNull
        @NotEmpty
        List<Long> variantItemIds
) {
        
}
