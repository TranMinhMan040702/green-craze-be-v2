package vn.com.greencraze.infrastructure.service;

import vn.com.greencraze.commons.api.RestResponse;
import vn.com.greencraze.infrastructure.dto.request.MessageRequest;
import vn.com.greencraze.infrastructure.dto.response.message.GetAllMessageByRoomIdResponse;

import java.util.List;

public interface IMessageService {
    RestResponse<List<GetAllMessageByRoomIdResponse>> getAllMessageByRoomId(Long roomId);

    // TODO: UpdateStatusMessage
    void updateStatusMessage(Long roomId);

    void sendMessage(String destination, MessageRequest request);

    @Deprecated
    void createMessage(MessageRequest request);

}
