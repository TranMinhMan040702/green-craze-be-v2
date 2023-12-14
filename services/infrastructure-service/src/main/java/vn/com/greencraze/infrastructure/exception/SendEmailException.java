package vn.com.greencraze.infrastructure.exception;

public class SendEmailException extends RuntimeException {

    public SendEmailException(String message) {
        super(message);
    }

}
