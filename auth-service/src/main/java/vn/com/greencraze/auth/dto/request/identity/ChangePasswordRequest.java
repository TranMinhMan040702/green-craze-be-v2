package vn.com.greencraze.auth.dto.request.identity;

public record ChangePasswordRequest(
        String oldPassword,
        String newPassword,
        String confirmPassword
) {}
