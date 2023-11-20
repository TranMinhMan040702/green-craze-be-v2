package vn.com.greencraze.order.dto.request.delivery;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record UpdateDeliveryRequest(
        @NotBlank
        String name,

        @NotBlank
        String code,

        @Nullable
        MultipartFile image,

        @NotNull
        Boolean status
) {
}
