package vn.com.greencraze.infrastructure.dto.request;

import lombok.Builder;

@Builder
public record MessageRequest(
        String userId,
        Long roomId,
        String image,
        Boolean status,
        String content
) {}
