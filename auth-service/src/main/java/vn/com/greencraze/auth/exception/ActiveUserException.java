package vn.com.greencraze.auth.exception;

public class ActiveUserException extends RuntimeException {

    public ActiveUserException() {
        super("User account is active");
    }

}
