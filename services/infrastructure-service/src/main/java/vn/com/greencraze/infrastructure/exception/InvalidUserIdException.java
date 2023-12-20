package vn.com.greencraze.infrastructure.exception;

public class InvalidUserIdException extends RuntimeException {

    public InvalidUserIdException(String message) {
        super(message);
    }

}
