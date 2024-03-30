package vn.com.greencraze.infrastructure.service;

import vn.com.greencraze.commons.api.RestResponse;
import vn.com.greencraze.infrastructure.dto.request.CreateRoomRequest;
import vn.com.greencraze.infrastructure.dto.response.room.CreateRoomResponse;
import vn.com.greencraze.infrastructure.dto.response.room.GetAllRoomResponse;
import vn.com.greencraze.infrastructure.dto.response.room.GetOneRoomByUserIdResponse;

import java.util.List;

public interface IRoomService {

    RestResponse<List<GetAllRoomResponse>> getAllRoom();

    RestResponse<GetOneRoomByUserIdResponse> getOneRoomByUserId(String userId);

    RestResponse<CreateRoomResponse> createRoom(CreateRoomRequest request);

}
