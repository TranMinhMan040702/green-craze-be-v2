package vn.com.greencraze.auth.exception;

public class UnconfirmedUserException extends RuntimeException {

    public UnconfirmedUserException() {
        super("User account is unconfirmed");
    }

}
