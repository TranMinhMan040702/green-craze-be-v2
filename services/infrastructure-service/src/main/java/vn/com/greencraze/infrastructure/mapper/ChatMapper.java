package vn.com.greencraze.infrastructure.mapper;

import org.mapstruct.Mapper;
import vn.com.greencraze.infrastructure.dto.request.CreateRoomRequest;
import vn.com.greencraze.infrastructure.dto.request.MessageRequest;
import vn.com.greencraze.infrastructure.dto.response.message.GetAllMessageByRoomIdResponse;
import vn.com.greencraze.infrastructure.dto.response.room.CreateRoomResponse;
import vn.com.greencraze.infrastructure.dto.response.room.GetAllRoomResponse;
import vn.com.greencraze.infrastructure.dto.response.room.GetOneRoomByUserIdResponse;
import vn.com.greencraze.infrastructure.entity.Message;
import vn.com.greencraze.infrastructure.entity.Room;

@Mapper
public interface ChatMapper {

    GetAllMessageByRoomIdResponse messageToGetAllMessageByRoomIdResponse(Message messages);

    GetAllRoomResponse roomToGetAllRoomResponse(Room room);

    GetOneRoomByUserIdResponse roomToGetOneRoomByUserIdResponse(Room room);

    GetOneRoomByUserIdResponse.MessageResponse messageToMessageResponse(Message message);

    Room createRoomRequestToRoom(CreateRoomRequest roomRequest);

    CreateRoomResponse roomToCreateRoomResponse(Room room);

    Message mesageRequestToMessage(MessageRequest messageRequest);

}
