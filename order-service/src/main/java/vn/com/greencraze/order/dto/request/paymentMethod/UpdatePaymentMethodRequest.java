package vn.com.greencraze.order.dto.request.paymentMethod;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record UpdatePaymentMethodRequest(
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
