package vn.com.greencraze.user.dto.request.user;

public record CreateUserRequest(
        String identityId,
        String email,
        String firstName,
        String lastName
) {}
