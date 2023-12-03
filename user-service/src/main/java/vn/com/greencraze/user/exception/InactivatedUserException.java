package vn.com.greencraze.user.exception;

public class InactivatedUserException extends RuntimeException {

    public InactivatedUserException() {
        super("User account is unactivated");
    }

}
