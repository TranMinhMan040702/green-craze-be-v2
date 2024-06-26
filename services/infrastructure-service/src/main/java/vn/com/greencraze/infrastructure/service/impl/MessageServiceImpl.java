package vn.com.greencraze.infrastructure.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import vn.com.greencraze.commons.api.RestResponse;
import vn.com.greencraze.commons.auth.AuthFacade;
import vn.com.greencraze.commons.exception.ResourceNotFoundException;
import vn.com.greencraze.infrastructure.dto.request.MessageRequest;
import vn.com.greencraze.infrastructure.dto.response.message.GetAllMessageByRoomIdResponse;
import vn.com.greencraze.infrastructure.entity.Message;
import vn.com.greencraze.infrastructure.mapper.ChatMapper;
import vn.com.greencraze.infrastructure.repository.MessageRepository;
import vn.com.greencraze.infrastructure.repository.RoomRepository;
import vn.com.greencraze.infrastructure.service.IMessageService;
import vn.com.greencraze.infrastructure.service.IUploadService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements IMessageService {

    private final MessageRepository messageRepository;
    private final RoomRepository roomRepository;

    private final IUploadService uploadService;

    private final ChatMapper chatMapper;

    private final AuthFacade authFacade;

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
    public void updateStatusMessage(Long roomId) {
        List<Message> messages;
        if (authFacade.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            messages = messageRepository.findByRoomIdAndUserIdNotLike(roomId, "ADMIN");
        } else {
            messages = messageRepository.findByRoomIdAndUserIdLike(roomId, "ADMIN");
        }

        for (Message message : messages) {
            message.setStatus(true);
            messageRepository.save(message);
        }
    }

    @Override
    public void sendMessage(String destination, MessageRequest request) {
        // create message
        Message message = chatMapper.mesageRequestToMessage(request);
        if (request.image() != null) {
            message.setImage(uploadService.uploadFile(request.image()));
        }
        message = messageRepository.save(message);
        // send message
        simpMessagingTemplate.convertAndSend("/chat/receive/" + destination,
                chatMapper.messageToMessageResponse(message));
    }

    @Deprecated
    @Override
    public void createMessage(MessageRequest request) {
        Message message = chatMapper.mesageRequestToMessage(request);
        if (request.image() != null) {
            message.setImage(uploadService.uploadFile(request.image()));
        }
        messageRepository.save(message);
    }

}
