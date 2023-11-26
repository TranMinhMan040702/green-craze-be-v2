package vn.com.greencraze.user.service;

import vn.com.greencraze.commons.api.ListResponse;
import vn.com.greencraze.commons.api.RestResponse;
import vn.com.greencraze.user.dto.request.user.CreateStaffRequest;
import vn.com.greencraze.user.dto.request.user.CreateUserRequest;
import vn.com.greencraze.user.dto.request.user.UpdateStaffRequest;
import vn.com.greencraze.user.dto.request.user.UpdateUserRequest;
import vn.com.greencraze.user.dto.response.user.CreateStaffResponse;
import vn.com.greencraze.user.dto.response.user.CreateUserResponse;
import vn.com.greencraze.user.dto.response.user.GetListStaffResponse;
import vn.com.greencraze.user.dto.response.user.GetListUserResponse;
import vn.com.greencraze.user.dto.response.user.GetMeResponse;
import vn.com.greencraze.user.dto.response.user.GetOneStaffResponse;
import vn.com.greencraze.user.dto.response.user.GetOneUserResponse;

import java.util.List;

public interface IUserProfileService {

    RestResponse<ListResponse<GetListUserResponse>> getListUser(
            Integer page, Integer size, Boolean isSortAscending, String columnName, String search, Boolean all);

    RestResponse<GetOneUserResponse> getOneUser(String id);

    RestResponse<GetMeResponse> getMe();

    RestResponse<CreateUserResponse> createUser(CreateUserRequest request);

    void updateUser(UpdateUserRequest request);

    void disableUser(String id);

    void enableUser(String id);

    void disableListUser(List<String> ids);

    RestResponse<GetListStaffResponse> getListStaff(
            Integer page, Integer size, Boolean isSortAscending, String columnName, String search, Boolean all);

    RestResponse<GetOneStaffResponse> getOneStaff(Long id);

    RestResponse<CreateStaffResponse> createStaff(CreateStaffRequest request);

    void updateStaff(Long id, UpdateStaffRequest request);

    void disableListStaff(List<Long> ids);

}
