package vn.com.greencraze.user.dto.request.user;

import org.springframework.web.multipart.MultipartFile;
import vn.com.greencraze.user.enumeration.GenderType;

public record UpdateUserRequest(
        String firstName,
        String lastName,
        String phone,
        String dob,
        GenderType gender,
        MultipartFile avatar
) {}
