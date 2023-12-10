package vn.com.greencraze.infrastructure.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/message") // => "/chatroom/message"
    @SendTo("/chatroom/public")
    public Message receiveMessagePublic(@Payload Message message) {
        return message;
    }

    @MessageMapping("/private-message") // => "/user/private-message"
    public Message receiveMessagePrivate(@Payload Message message) {
        simpMessagingTemplate.convertAndSendToUser(message.receiverName(), "/private", message);
        return message;
    }

}
