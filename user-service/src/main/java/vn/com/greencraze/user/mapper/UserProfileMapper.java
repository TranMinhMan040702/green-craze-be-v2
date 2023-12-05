package vn.com.greencraze.user.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import vn.com.greencraze.user.client.address.dto.response.GetListAddressByUserIdResponse;
import vn.com.greencraze.user.dto.request.user.CreateStaffRequest;
import vn.com.greencraze.user.dto.request.user.CreateUserRequest;
import vn.com.greencraze.user.dto.request.user.UpdateUserRequest;
import vn.com.greencraze.user.dto.response.user.AddressUserResponse;
import vn.com.greencraze.user.dto.response.user.CreateStaffResponse;
import vn.com.greencraze.user.dto.response.user.CreateUserResponse;
import vn.com.greencraze.user.dto.response.user.GetListStaffResponse;
import vn.com.greencraze.user.dto.response.user.GetListUserResponse;
import vn.com.greencraze.user.dto.response.user.GetMeResponse;
import vn.com.greencraze.user.dto.response.user.GetOneStaffResponse;
import vn.com.greencraze.user.dto.response.user.GetOneUserResponse;
import vn.com.greencraze.user.entity.Staff;
import vn.com.greencraze.user.entity.UserProfile;
import vn.com.greencraze.user.entity.view.RoleView;

@Mapper
public interface UserProfileMapper {

    @Mapping(source = "identity.status", target = "status")
    @Mapping(source = "identity.roles", target = "roles")
    GetListUserResponse userProfileToGetListUserResponse(UserProfile userProfile);

    @Mapping(source = "identity.status", target = "status")
    @Mapping(source = "identity.roles", target = "roles")
    GetOneUserResponse userProfileToGetOneUserResponse(UserProfile userProfile);

    @Mapping(source = "identity.status", target = "status")
    @Mapping(source = "identity.roles", target = "roles")
    GetMeResponse userProfileToGetMeResponse(UserProfile userProfile);

    UserProfile createUserRequestToUserProfile(CreateUserRequest createUserRequest);

    CreateUserResponse userProfileToCreateUserResponse(UserProfile userProfile);

    @Mapping(target = "avatar", ignore = true)
    UserProfile updateUserProfileFromUpdateUserRequest(
            @MappingTarget UserProfile userProfile, UpdateUserRequest updateUserRequest);

    @Mapping(source = "user.email", target = "email")
    @Mapping(source = "user.firstName", target = "firstName")
    @Mapping(source = "user.lastName", target = "lastName")
    @Mapping(source = "user.phone", target = "phone")
    @Mapping(source = "user.dob", target = "dob")
    @Mapping(source = "user.gender", target = "gender")
    @Mapping(source = "user.avatar", target = "avatar")
    @Mapping(source = "user.identity.status", target = "status")
    @Mapping(source = "user.identity.roles", target = "roles")
    GetListStaffResponse userProfileToGetListStaffResponse(Staff staff);

    @Mapping(source = "staff.type", target = "type")
    @Mapping(source = "staff.code", target = "code")
    @Mapping(source = "identity.status", target = "status")
    @Mapping(source = "identity.roles", target = "roles")
    GetOneStaffResponse userProfileToGetOneStaffResponse(UserProfile userProfile);

    UserProfile createStaffRequestToUserProfile(CreateStaffRequest createStaffRequest);

    @Mapping(source = "identity.status", target = "status")
    @Mapping(source = "identity.roles", target = "roles")
    CreateStaffResponse userProfileToCreateStaffResponse(UserProfile userProfile);

    @BeanMapping(qualifiedByName = "getRoleName")
    String roleToName(RoleView role);

    @BeforeMapping
    @Named("getRoleName")
    default String getRoleName(RoleView role) {
        return role.getName();
    }

    AddressUserResponse getListAddressByUserIdResponseToAddressUserResponse(
            GetListAddressByUserIdResponse getListAddressByUserIdResponse);

}
