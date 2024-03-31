package vn.com.greencraze.infrastructure.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import vn.com.greencraze.commons.api.RestResponse;
import vn.com.greencraze.infrastructure.dto.request.CreateRoomRequest;
import vn.com.greencraze.infrastructure.dto.request.MessageRequest;
import vn.com.greencraze.infrastructure.dto.response.message.GetAllMessageByRoomIdResponse;
import vn.com.greencraze.infrastructure.dto.response.room.CreateRoomResponse;
import vn.com.greencraze.infrastructure.dto.response.room.GetAllRoomResponse;
import vn.com.greencraze.infrastructure.dto.response.room.GetOneRoomByUserIdResponse;
import vn.com.greencraze.infrastructure.service.IMessageService;
import vn.com.greencraze.infrastructure.service.IRoomService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/chat")
@Tag(name = "chat :: Chat")
@RequiredArgsConstructor
public class ChatController {

    private final IMessageService messageService;
    private final IRoomService roomService;

    @MessageMapping("/send/message")
    public void sendMessage(@Payload MessageRequest request) {
        messageService.sendMessage(request.destination(), request);
    }

    @PostMapping(
            value = "/send/message",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Send message with image")
    public void sendMessageWithImage(@Valid MessageRequest request) {
        messageService.sendMessage(request.destination(), request);
    }

    //TODO: Role Admin
    @GetMapping("/rooms")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get all room")
    public ResponseEntity<RestResponse<List<GetAllRoomResponse>>> getAllRoom() {
        return ResponseEntity.ok(roomService.getAllRoom());
    }

    @GetMapping("/rooms/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get one room by user id")
    public ResponseEntity<RestResponse<GetOneRoomByUserIdResponse>> getOneRoomByUserId(@PathVariable String userId) {
        return ResponseEntity.ok(roomService.getOneRoomByUserId(userId));
    }

    @Deprecated
    @PostMapping(
            value = "/rooms",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a room")
    public ResponseEntity<RestResponse<CreateRoomResponse>> createRoom(@RequestBody @Valid CreateRoomRequest request) {
        RestResponse<CreateRoomResponse> response = roomService.createRoom(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(response.data().id()).toUri();

        MessageRequest messageRequest = MessageRequest.builder()
                .userId("ADMIN")    // TODO
                .roomId(response.data().id())
                .image(null)
                .status(true)
                .content("Cảm ơn bạn đã quan tâm đến Shop. Bạn có câu hỏi gì cho chúng tôi.")
                .build();
        messageService.sendMessage(request.userId(), messageRequest);

        return ResponseEntity.created(location).body(response);
    }

    @GetMapping("/messages/{roomId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get all message by room id")
    public ResponseEntity<RestResponse<List<GetAllMessageByRoomIdResponse>>> getAllMessageByRoomId(
            @PathVariable Long roomId) {
        return ResponseEntity.ok(messageService.getAllMessageByRoomId(roomId));
    }

}
