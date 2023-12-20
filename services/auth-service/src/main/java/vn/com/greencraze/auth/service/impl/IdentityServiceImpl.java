package vn.com.greencraze.auth.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.com.greencraze.auth.dto.request.identity.ChangePasswordRequest;
import vn.com.greencraze.auth.dto.request.identity.CreateIdentityRequest;
import vn.com.greencraze.auth.dto.request.identity.DisableListIdentityRequest;
import vn.com.greencraze.auth.dto.request.identity.UpdateIdentityRequest;
import vn.com.greencraze.auth.dto.request.identity.UpdateIdentityStatusRequest;
import vn.com.greencraze.auth.dto.response.user.CreateIdentityResponse;
import vn.com.greencraze.auth.entity.Identity;
import vn.com.greencraze.auth.entity.view.UserProfileView;
import vn.com.greencraze.auth.enumeration.IdentityStatus;
import vn.com.greencraze.auth.enumeration.RoleCode;
import vn.com.greencraze.auth.exception.InvalidPasswordException;
import vn.com.greencraze.auth.repository.IdentityRepository;
import vn.com.greencraze.auth.repository.RoleRepository;
import vn.com.greencraze.auth.repository.view.UserProfileViewRepository;
import vn.com.greencraze.auth.service.IIdentityService;
import vn.com.greencraze.commons.api.RestResponse;
import vn.com.greencraze.commons.auth.AuthFacade;
import vn.com.greencraze.commons.exception.ResourceNotFoundException;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class IdentityServiceImpl implements IIdentityService {

    private final IdentityRepository identityRepository;
    private final RoleRepository roleRepository;
    private final UserProfileViewRepository userProfileViewRepository;

    private final AuthFacade authFacade;

    private final PasswordEncoder passwordEncoder;

    private static final String RESOURCE_NAME = "Identity";

    @Override
    public void updateIdentityStatus(UpdateIdentityStatusRequest request) {
        Identity identity = identityRepository.findById(request.id())
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "identityId", ""));
        identity.setStatus(request.status());
        identityRepository.save(identity);
    }

    @Transactional(rollbackOn = {ResourceNotFoundException.class})
    @Override
    public void disableListIdentity(DisableListIdentityRequest request) {
        for (String id : request.ids()) {
            Identity identity = identityRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "identityId", ""));
            identity.setStatus(IdentityStatus.INACTIVE);
            identityRepository.save(identity);
        }
    }

    @Override
    public RestResponse<CreateIdentityResponse> createIdentity(CreateIdentityRequest request) {
        Identity identity = Identity.builder()
                .username(request.email())
                .password(passwordEncoder.encode(request.password()))
                .status(IdentityStatus.ACTIVE)
                .roles(Set.of(roleRepository.getReferenceByCode(RoleCode.STAFF.toString()),
                        roleRepository.getReferenceByCode(RoleCode.USER.toString())))
                .build();
        identityRepository.save(identity);

        return RestResponse.created(CreateIdentityResponse.builder()
                .id(identity.getId())
                .build());
    }

    @Override
    public void updateIdentity(UpdateIdentityRequest request) {
        Identity identity = identityRepository.findById(request.id())
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "identityId", ""));
        identity.setPassword(passwordEncoder.encode(request.password()));
        identityRepository.save(identity);
    }

    @Override
    public void changePassword(ChangePasswordRequest request) {
        String userId = authFacade.getUserId();
        Identity identity = userProfileViewRepository.findById(userId)
                .map(UserProfileView::getIdentity)
                .orElseThrow(() -> new ResourceNotFoundException("UserId", "id", userId));
        if (passwordEncoder.matches(identity.getPassword(), request.oldPassword())) {
            throw new InvalidPasswordException("Old password invalid");
        }

        if (!request.newPassword().equals(request.confirmPassword())) {
            throw new InvalidPasswordException("Confirm password does not match");
        }

        identity.setPassword(passwordEncoder.encode(request.newPassword()));
        identityRepository.save(identity);
    }

}
