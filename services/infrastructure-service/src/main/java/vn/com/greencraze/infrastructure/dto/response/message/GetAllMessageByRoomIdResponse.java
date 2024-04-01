package vn.com.greencraze.infrastructure.dto.response.message;

public record GetAllMessageByRoomIdResponse(
        Long id,
        String userId,
        String image,
        Boolean status,
        String content
) {}
