package vn.com.greencraze.auth.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.com.greencraze.auth.client.user.UserServiceClient;
import vn.com.greencraze.auth.client.user.dto.CreateUserRequest;
import vn.com.greencraze.auth.entity.Identity;
import vn.com.greencraze.auth.entity.Role;
import vn.com.greencraze.auth.enumeration.IdentityStatus;
import vn.com.greencraze.auth.enumeration.RoleCode;
import vn.com.greencraze.auth.repository.IdentityRepository;
import vn.com.greencraze.auth.repository.RoleRepository;
import vn.com.greencraze.auth.service.ISystemService;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class SystemServiceImpl implements ISystemService {

    private final IdentityRepository identityRepository;
    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserServiceClient userServiceClient;

    @Override
    public void createAccountAdmin() {
        Identity identity = Identity.builder()
                .username("admin@gmail.com")
                .password(passwordEncoder.encode("123456"))
                .roles(Set.of(roleRepository.getReferenceByCode(RoleCode.USER.toString()),
                        roleRepository.getReferenceByCode(RoleCode.ADMIN.toString())))
                .status(IdentityStatus.ACTIVE)
                .build();

        identityRepository.save(identity);

        userServiceClient.createUser(CreateUserRequest.builder()
                .email("admin@gmail.com")
                .firstName("Admin")
                .lastName("Admin")
                .identityId(identity.getId())
                .build());

    }

    @Override
    public void createRole() {
        Role roleAdmin = Role.builder()
                .name(RoleCode.ADMIN.toString())
                .code(RoleCode.ADMIN.toString())
                .build();

        Role roleStaff = Role.builder()
                .name(RoleCode.STAFF.toString())
                .code(RoleCode.STAFF.toString())
                .build();

        Role roleUser = Role.builder()
                .name(RoleCode.USER.toString())
                .code(RoleCode.USER.toString())
                .build();

        roleRepository.save(roleAdmin);
        roleRepository.save(roleStaff);
        roleRepository.save(roleUser);
    }

}
