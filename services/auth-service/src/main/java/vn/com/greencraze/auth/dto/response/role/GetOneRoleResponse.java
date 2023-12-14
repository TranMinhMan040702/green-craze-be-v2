package vn.com.greencraze.auth.dto.response.role;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.lang.Nullable;

import java.time.Instant;

public record GetOneRoleResponse(
        String id,
        Instant createdAt,
        Instant updatedAt,
        @Nullable
        @Schema(nullable = true)
        String createdBy,
        @Nullable
        @Schema(nullable = true)
        String updatedBy,
        String name
) {}
