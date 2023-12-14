package vn.com.greencraze.user.exception;

public class UnconfirmedUserException extends RuntimeException {

    public UnconfirmedUserException() {
        super("User account is unconfirmed");
    }

}
