package vn.com.greencraze.user.dto.response.user;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.lang.Nullable;
import vn.com.greencraze.user.enumeration.GenderType;
import vn.com.greencraze.user.enumeration.IdentityStatus;

import java.time.Instant;
import java.util.List;

public record GetListUserResponse(
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
        List<String> roles,
        List<AddressUserResponse> addresses
) {}
