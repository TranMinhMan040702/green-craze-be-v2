package vn.com.greencraze.auth.exception;

public class InvalidPasswordException extends RuntimeException {

    public InvalidPasswordException() {
        super("Password is invalid");
    }

}
