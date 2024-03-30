package vn.com.greencraze.infrastructure.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.greencraze.commons.api.RestResponse;
import vn.com.greencraze.commons.exception.ResourceNotFoundException;
import vn.com.greencraze.infrastructure.dto.request.CreateRoomRequest;
import vn.com.greencraze.infrastructure.dto.response.room.CreateRoomResponse;
import vn.com.greencraze.infrastructure.dto.response.room.GetAllRoomResponse;
import vn.com.greencraze.infrastructure.dto.response.room.GetOneRoomByUserIdResponse;
import vn.com.greencraze.infrastructure.entity.Room;
import vn.com.greencraze.infrastructure.mapper.ChatMapper;
import vn.com.greencraze.infrastructure.repository.RoomRepository;
import vn.com.greencraze.infrastructure.service.IRoomService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements IRoomService {

    private final RoomRepository roomRepository;

    private final ChatMapper chatMapper;

    private static final String RESOURCE_NAME = "Room";

    @Override
    public RestResponse<List<GetAllRoomResponse>> getAllRoom() {
        List<GetAllRoomResponse> responses = roomRepository.findAll().stream()
                .map(chatMapper::roomToGetAllRoomResponse).toList();
        return RestResponse.ok(responses);
    }

    @Override
    public RestResponse<GetOneRoomByUserIdResponse> getOneRoomByUserId(String userId) {
        // check room exist
        Room room = roomRepository.findByUserId(userId);
        if (room == null) {
            throw new ResourceNotFoundException(RESOURCE_NAME, "userId", userId);
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
