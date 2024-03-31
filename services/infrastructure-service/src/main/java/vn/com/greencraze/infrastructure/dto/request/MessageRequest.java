package vn.com.greencraze.infrastructure.dto.request;

import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

@Builder
public record MessageRequest(
        String destination,
        String userId,
        Long roomId,
        MultipartFile image,
        Boolean status,
        String content
) {}
