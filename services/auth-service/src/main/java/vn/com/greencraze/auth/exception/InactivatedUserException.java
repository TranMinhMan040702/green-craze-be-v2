package vn.com.greencraze.auth.exception;

public class InactivatedUserException extends RuntimeException {

    public InactivatedUserException() {
        super("User account is unactivated");
    }

}
