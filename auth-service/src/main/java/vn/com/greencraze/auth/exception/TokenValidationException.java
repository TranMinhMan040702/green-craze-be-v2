package vn.com.greencraze.auth.exception;

public class TokenValidationException extends RuntimeException {

    public TokenValidationException(String message) {
        super(message);
    }

}
