package vn.com.greencraze.infrastructure.mapper;

import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import vn.com.greencraze.commons.mapper.ReferenceMapper;
import vn.com.greencraze.infrastructure.dto.request.CreateRoomRequest;
import vn.com.greencraze.infrastructure.dto.request.MessageRequest;
import vn.com.greencraze.infrastructure.dto.request.MessageResponse;
import vn.com.greencraze.infrastructure.dto.response.message.GetAllMessageByRoomIdResponse;
import vn.com.greencraze.infrastructure.dto.response.room.CreateRoomResponse;
import vn.com.greencraze.infrastructure.dto.response.room.GetAllRoomResponse;
import vn.com.greencraze.infrastructure.dto.response.room.GetOneRoomByUserIdResponse;
import vn.com.greencraze.infrastructure.entity.Message;
import vn.com.greencraze.infrastructure.entity.Room;

import java.util.List;

@Mapper(uses = {ReferenceMapper.class})
public interface ChatMapper {

    @Mapping(target = "id", ignore = true)
    Room idToRoom(String id);

    GetAllMessageByRoomIdResponse messageToGetAllMessageByRoomIdResponse(Message messages);

    @Mapping(target = "message", expression = "java(getLastMessage(room.getMessages()))")
    GetAllRoomResponse roomToGetAllRoomResponse(Room room);

    GetOneRoomByUserIdResponse roomToGetOneRoomByUserIdResponse(Room room);

    Room createRoomRequestToRoom(CreateRoomRequest roomRequest);

    CreateRoomResponse roomToCreateRoomResponse(Room room);

    @Mapping(target = "image", ignore = true)
    @Mapping(source = "roomId", target = "room")
    Message mesageRequestToMessage(MessageRequest messageRequest);

    MessageResponse messageToMessageResponse(Message message);

    @BeforeMapping
    default MessageResponse getLastMessage(List<Message> messages) {
        if (messages == null || messages.isEmpty()) {
            return null;
        }
        // Assuming MessageResponse is a class that maps from Message
        return messageToMessageResponse(messages.get(messages.size() - 1));
    }

}
