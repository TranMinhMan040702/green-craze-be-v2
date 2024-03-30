package vn.com.greencraze.infrastructure.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import vn.com.greencraze.commons.api.RestResponse;
import vn.com.greencraze.commons.exception.ResourceNotFoundException;
import vn.com.greencraze.infrastructure.dto.request.MessageRequest;
import vn.com.greencraze.infrastructure.dto.response.message.GetAllMessageByRoomIdResponse;
import vn.com.greencraze.infrastructure.entity.Message;
import vn.com.greencraze.infrastructure.mapper.ChatMapper;
import vn.com.greencraze.infrastructure.repository.MessageRepository;
import vn.com.greencraze.infrastructure.repository.RoomRepository;
import vn.com.greencraze.infrastructure.service.IMessageService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements IMessageService {

    private final MessageRepository messageRepository;
    private final RoomRepository roomRepository;

    private final ChatMapper chatMapper;

    private final SimpMessagingTemplate simpMessagingTemplate;

    private static final String RESOURCE_NAME = "Room";

    @Override
    public RestResponse<List<GetAllMessageByRoomIdResponse>> getAllMessageByRoomId(Long roomId) {
        // check room exist
        if (!roomRepository.existsById(roomId)) {
            throw new ResourceNotFoundException(RESOURCE_NAME, "id", roomId);
        }

        // get all message by room id
        List<GetAllMessageByRoomIdResponse> responses = messageRepository.findAllByRoomId(roomId).stream()
                .map(chatMapper::messageToGetAllMessageByRoomIdResponse)
                .toList();

        return RestResponse.ok(responses);
    }

    @Override
    public void sendMessage(String destination, MessageRequest request) {
        simpMessagingTemplate.convertAndSend("/chat/receive/" + destination, request);
    }

    @Override
    public void createMessage(MessageRequest request) {
        Message message = chatMapper.mesageRequestToMessage(request);
        messageRepository.save(message);
    }

}
