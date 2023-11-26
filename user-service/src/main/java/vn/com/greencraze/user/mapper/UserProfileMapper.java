package vn.com.greencraze.user.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import vn.com.greencraze.user.dto.request.user.CreateStaffRequest;
import vn.com.greencraze.user.dto.request.user.CreateUserRequest;
import vn.com.greencraze.user.dto.request.user.UpdateUserRequest;
import vn.com.greencraze.user.dto.response.user.CreateStaffResponse;
import vn.com.greencraze.user.dto.response.user.CreateUserResponse;
import vn.com.greencraze.user.dto.response.user.GetListStaffResponse;
import vn.com.greencraze.user.dto.response.user.GetListUserResponse;
import vn.com.greencraze.user.dto.response.user.GetMeResponse;
import vn.com.greencraze.user.dto.response.user.GetOneStaffResponse;
import vn.com.greencraze.user.dto.response.user.GetOneUserResponse;
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

    @Mapping(source = "staff.type", target = "type")
    @Mapping(source = "staff.code", target = "code")
    @Mapping(source = "identity.status", target = "status")
    @Mapping(source = "identity.roles", target = "roles")
    GetListStaffResponse userProfileToGetListStaffResponse(UserProfile userProfile);

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

}
