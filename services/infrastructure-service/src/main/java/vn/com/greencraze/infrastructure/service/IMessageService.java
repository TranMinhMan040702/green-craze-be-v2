package vn.com.greencraze.infrastructure.service;

import vn.com.greencraze.commons.api.RestResponse;
import vn.com.greencraze.infrastructure.dto.request.MessageRequest;
import vn.com.greencraze.infrastructure.dto.response.message.GetAllMessageByRoomIdResponse;

import java.util.List;

public interface IMessageService {
    // TODO: GetListMessageByRoomID
    RestResponse<List<GetAllMessageByRoomIdResponse>> getAllMessageByRoomId(Long roomId);

    // TODO: UpdateStatusMessage

    void sendMessage(String destination, MessageRequest request);

    void createMessage(MessageRequest request);

}
