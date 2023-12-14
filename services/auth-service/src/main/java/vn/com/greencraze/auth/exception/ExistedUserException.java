package vn.com.greencraze.auth.exception;

public class ExistedUserException extends RuntimeException {

    public ExistedUserException() {
        super("User account is existed");
    }

}
