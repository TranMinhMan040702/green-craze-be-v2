package vn.com.greencraze.user.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.com.greencraze.commons.api.ListResponse;
import vn.com.greencraze.commons.api.RestResponse;
import vn.com.greencraze.commons.auth.AuthFacade;
import vn.com.greencraze.commons.exception.ResourceNotFoundException;
import vn.com.greencraze.user.client.auth.AuthServiceClient;
import vn.com.greencraze.user.client.auth.dto.request.CreateIdentityRequest;
import vn.com.greencraze.user.client.auth.dto.request.DisableListIdentityRequest;
import vn.com.greencraze.user.client.auth.dto.request.UpdateIdentityRequest;
import vn.com.greencraze.user.client.auth.dto.request.UpdateIdentityStatusRequest;
import vn.com.greencraze.user.client.auth.dto.response.CreateIdentityResponse;
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
import vn.com.greencraze.user.entity.Cart;
import vn.com.greencraze.user.entity.Staff;
import vn.com.greencraze.user.entity.UserProfile;
import vn.com.greencraze.user.entity.view.IdentityView;
import vn.com.greencraze.user.enumeration.GenderType;
import vn.com.greencraze.user.enumeration.IdentityStatus;
import vn.com.greencraze.user.exception.InactivatedUserException;
import vn.com.greencraze.user.exception.UnconfirmedUserException;
import vn.com.greencraze.user.mapper.UserProfileMapper;
import vn.com.greencraze.user.repository.StaffRepository;
import vn.com.greencraze.user.repository.UserProfileRepository;
import vn.com.greencraze.user.repository.specification.StaffSpecification;
import vn.com.greencraze.user.repository.specification.UserProfileSpecification;
import vn.com.greencraze.user.repository.view.IdentityViewRepository;
import vn.com.greencraze.user.service.IUploadService;
import vn.com.greencraze.user.service.IUserProfileService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserProfileServiceImpl implements IUserProfileService {

    private final UserProfileRepository userProfileRepository;
    private final StaffRepository staffRepository;
    private final IdentityViewRepository identityViewRepository;
    private final UserProfileMapper userProfileMapper;
    private final IUploadService uploadService;
    private final AuthFacade authFacade;
    private final AuthServiceClient authServiceClient;
    private static final String RESOURCE_NAME = "User";
    private static final List<String> SEARCH_FIELDS = List.of("name");

    @Override
    public RestResponse<ListResponse<GetListUserResponse>> getListUser(
            Integer page, Integer size, Boolean isSortAscending, String columnName, String search, Boolean all
    ) {
        UserProfileSpecification userProfileSpecification = new UserProfileSpecification();
        Specification<UserProfile> sortable = userProfileSpecification.sortable(isSortAscending, columnName);
        Specification<UserProfile> searchable = userProfileSpecification.searchable(SEARCH_FIELDS, search);
        Pageable pageable = all ? Pageable.unpaged() : PageRequest.of(page - 1, size);
        Page<GetListUserResponse> responses = userProfileRepository
                .findAll(sortable.and(searchable), pageable)
                .map(userProfileMapper::userProfileToGetListUserResponse);
        // todo: set address

        return RestResponse.ok(ListResponse.of(responses));
    }

    @Override
    public RestResponse<GetOneUserResponse> getOneUser(String id) {
        // todo: Set address
        return userProfileRepository.findById(id)
                .map(userProfileMapper::userProfileToGetOneUserResponse)
                .map(RestResponse::ok)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "id", id));
    }

    @Override
    public RestResponse<GetMeResponse> getMe() {
        String userId = authFacade.getUserId();
        return userProfileRepository.findById(userId)
                .map(userProfileMapper::userProfileToGetMeResponse)
                .map(RestResponse::ok)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "identityId", ""));
    }

    @Override
    public RestResponse<CreateUserResponse> createUser(CreateUserRequest request) {
        IdentityView identityView = identityViewRepository.getReferenceById(request.identityId());
        UserProfile userProfile = userProfileMapper.createUserRequestToUserProfile(request);
        userProfile.setIdentity(identityView);
        userProfile.setGender(GenderType.OTHER);
        userProfile.setCart(Cart.builder().user(userProfile).build());
        userProfileRepository.save(userProfile);
        return RestResponse.created(userProfileMapper.userProfileToCreateUserResponse(userProfile));
    }

    @Override
    public void updateUser(UpdateUserRequest request) {
        String userId = authFacade.getUserId();
        UserProfile userProfile = userProfileRepository.findById(userId)
                .map(u -> userProfileMapper.updateUserProfileFromUpdateUserRequest(u, request))
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "identityId", ""));
        if (request.avatar() != null) {
            userProfile.setAvatar(uploadService.uploadFile(request.avatar()));
        }
        userProfileRepository.save(userProfile);
    }

    @Override
    public void disableUser(String id) {
        UserProfile userProfile = userProfileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "id", id));

        authServiceClient.updateIdentityStatus(UpdateIdentityStatusRequest.builder()
                .id(userProfile.getIdentity().getId())
                .status(IdentityStatus.INACTIVE)
                .build());
    }

    @Override
    public void enableUser(String id) {
        UserProfile userProfile = userProfileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "id", id));

        authServiceClient.updateIdentityStatus(UpdateIdentityStatusRequest.builder()
                .id(userProfile.getIdentity().getId())
                .status(IdentityStatus.ACTIVE)
                .build());
    }

    @Override
    public void toggleUserStatus(String id) {
        UserProfile userProfile = userProfileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "id", id));

        authServiceClient.updateIdentityStatus(UpdateIdentityStatusRequest.builder()
                .id(userProfile.getIdentity().getId())
                .status(switch (userProfile.getIdentity().getStatus()) {
                    case ACTIVE -> IdentityStatus.BLOCK;
                    case BLOCK -> IdentityStatus.ACTIVE;
                    case INACTIVE -> throw new InactivatedUserException();
                    case UNCONFIRMED -> throw new UnconfirmedUserException();
                })
                .build());
    }

    @Transactional(rollbackOn = {ResourceNotFoundException.class})
    @Override
    public void disableListUser(List<String> ids) {
        List<String> identityIds = new ArrayList<>();
        for (String id : ids) {
            UserProfile userProfile = userProfileRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "id", id));
            identityIds.add(userProfile.getIdentity().getId());
        }

        authServiceClient.disableListIdentity(DisableListIdentityRequest.builder()
                .ids(identityIds)
                .build());
    }

    @Override
    public RestResponse<ListResponse<GetListStaffResponse>> getListStaff(
            Integer page, Integer size, Boolean isSortAscending, String columnName, String search, Boolean all) {
        StaffSpecification staffSpecification = new StaffSpecification();
        Specification<Staff> sortable = staffSpecification.sortable(isSortAscending, columnName);
        Specification<Staff> searchable = staffSpecification.searchable(SEARCH_FIELDS, search);
        Pageable pageable = all ? Pageable.unpaged() : PageRequest.of(page - 1, size);
        Page<GetListStaffResponse> responses = staffRepository
                .findAll(sortable.and(searchable), pageable)
                .map(userProfileMapper::userProfileToGetListStaffResponse);
        //todo: map address
        return RestResponse.ok(ListResponse.of(responses));
    }

    @Override
    public RestResponse<GetOneStaffResponse> getOneStaff(Long id) {
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Staff", "id", id));
        return RestResponse.ok(userProfileMapper.userProfileToGetOneStaffResponse(staff.getUser()));
    }

    @Override
    public RestResponse<CreateStaffResponse> createStaff(CreateStaffRequest request) {
        CreateIdentityResponse identity = authServiceClient.createIdentity(CreateIdentityRequest.builder()
                .email(request.email())
                .password(request.password())
                .build());
        IdentityView identityView = identityViewRepository.getReferenceById(identity.id());

        UserProfile userProfile = userProfileMapper.createStaffRequestToUserProfile(request);
        userProfile.setIdentity(identityView);
        userProfile.setCart(Cart.builder().user(userProfile).build());
        userProfile.setStaff(Staff.builder()
                .type(request.type())
                .code(UUID.randomUUID().toString())
                .build());
        userProfileRepository.save(userProfile);

        // todo: call address service create address for staff

        return RestResponse.created(userProfileMapper.userProfileToCreateStaffResponse(userProfile));
    }

    @Override
    public void updateStaff(Long id, UpdateStaffRequest request) {
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Staff", "id", id));

        staff.setType(request.type());
        staffRepository.save(staff);

        authServiceClient.updateIdentity(UpdateIdentityRequest.builder()
                .id(staff.getUser().getIdentity().getId())
                .password(request.password())
                .build());

        // todo: call address service update address for staff
    }

    @Transactional(rollbackOn = {ResourceNotFoundException.class})
    @Override
    public void disableListStaff(List<Long> ids) {
        List<String> identityIds = new ArrayList<>();
        for (Long id : ids) {
            Staff staff = staffRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Staff", "id", id));
            identityIds.add(staff.getUser().getIdentity().getId());
        }

        authServiceClient.disableListIdentity(DisableListIdentityRequest.builder()
                .ids(identityIds)
                .build());
    }

    @Override
    public void disableStaff(Long id) {
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Staff", "id", id));

        authServiceClient.updateIdentityStatus(UpdateIdentityStatusRequest.builder()
                .id(staff.getUser().getIdentity().getId())
                .status(IdentityStatus.INACTIVE)
                .build());
    }

    @Override
    public void enableStaff(Long id) {
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Staff", "id", id));

        authServiceClient.updateIdentityStatus(UpdateIdentityStatusRequest.builder()
                .id(staff.getUser().getIdentity().getId())
                .status(IdentityStatus.ACTIVE)
                .build());
    }

    @Override
    public void toggleStaffStatus(Long id) {
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Staff", "id", id));

        authServiceClient.updateIdentityStatus(UpdateIdentityStatusRequest.builder()
                .id(staff.getUser().getIdentity().getId())
                .status(switch (staff.getUser().getIdentity().getStatus()) {
                    case ACTIVE -> IdentityStatus.BLOCK;
                    case BLOCK -> IdentityStatus.ACTIVE;
                    case INACTIVE -> throw new InactivatedUserException();
                    case UNCONFIRMED -> throw new UnconfirmedUserException();
                })
                .build());
    }

}
