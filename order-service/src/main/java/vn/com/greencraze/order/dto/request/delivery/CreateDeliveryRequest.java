package vn.com.greencraze.order.dto.request.delivery;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record CreateDeliveryRequest(
        @NotBlank
        String name,

        @NotBlank
        String code,

        @NotNull
        MultipartFile image
) {
}
