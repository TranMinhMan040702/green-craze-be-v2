package vn.com.greencraze.infrastructure.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import vn.com.greencraze.commons.api.RestResponse;
import vn.com.greencraze.infrastructure.client.user.UserServiceClient;
import vn.com.greencraze.infrastructure.dto.request.CreateRoomRequest;
import vn.com.greencraze.infrastructure.dto.response.room.CreateRoomResponse;
import vn.com.greencraze.infrastructure.dto.response.room.GetAllRoomResponse;
import vn.com.greencraze.infrastructure.dto.response.room.GetOneRoomByUserIdResponse;
import vn.com.greencraze.infrastructure.entity.Message;
import vn.com.greencraze.infrastructure.entity.Room;
import vn.com.greencraze.infrastructure.mapper.ChatMapper;
import vn.com.greencraze.infrastructure.repository.RoomRepository;
import vn.com.greencraze.infrastructure.repository.specification.RoomSpecification;
import vn.com.greencraze.infrastructure.service.IRoomService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements IRoomService {

    private final RoomRepository roomRepository;

    private final ChatMapper chatMapper;

    private final RoomSpecification roomSpecification;

    private final UserServiceClient userServiceClient;

    private final SimpMessagingTemplate simpMessagingTemplate;

    private static final String RESOURCE_NAME = "Room";

    @Override
    public RestResponse<List<GetAllRoomResponse>> getAllRoom() {
        Specification<Room> sortable = roomSpecification.orderByLatestMessageUpdatedAt();
        List<GetAllRoomResponse> responses = roomRepository.findAll(sortable).stream()
                .map(chatMapper::roomToGetAllRoomResponse).toList();
        return RestResponse.ok(responses);
    }

    @Override
    public RestResponse<GetOneRoomByUserIdResponse> getOneRoomByUserId(String userId) {
        // check room exist
        Room room = roomRepository.findByUserId(userId);
        if (room == null) {
            String username = userServiceClient.getUsername(userId);
            room = Room.builder()
                    .userId(userId)
                    .name(username)
                    .build();

            Message message = Message.builder()
                    .userId("ADMIN")    // TODO
                    .room(room)
                    .image(null)
                    .status(true)
                    .content("Cảm ơn bạn đã quan tâm đến Shop. Bạn có câu hỏi gì cho chúng tôi.")
                    .build();

            room.setMessages(List.of(message));
            roomRepository.save(room);
            simpMessagingTemplate.convertAndSend("/chat/receive/" + userId, message);
        }

        return RestResponse.ok(chatMapper.roomToGetOneRoomByUserIdResponse(room));
    }

    @Override
    public RestResponse<CreateRoomResponse> createRoom(CreateRoomRequest request) {
        Room room = chatMapper.createRoomRequestToRoom(request);
        roomRepository.save(room);
        return RestResponse.created(chatMapper.roomToCreateRoomResponse(room));
    }

}
