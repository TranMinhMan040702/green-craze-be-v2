package vn.com.greencraze.order.client.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.lang.Nullable;

import java.time.Instant;
import java.util.List;

public record GetOneUserResponse(
        String id,
        Instant createdAt,
        Instant updatedAt,
        @Nullable
        @Schema(nullable = true)
        String createdBy,
        @Nullable
        @Schema(nullable = true)
        String updatedBy,
        String email,
        String firstName,
        String lastName,
        @Nullable
        String phone,
        Instant dob,
        GenderType gender,
        @Nullable
        @Schema(nullable = true)
        String avatar,
        IdentityStatus status,
        List<String> roles
) {
    public enum GenderType {
        MALE,
        FEMALE,
        OTHER
    }

    public enum IdentityStatus {
        ACTIVE,
        INACTIVE,
        UNCONFIRMED,
        BLOCK
    }
}
