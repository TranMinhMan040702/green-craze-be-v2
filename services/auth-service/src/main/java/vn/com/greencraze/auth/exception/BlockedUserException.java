package vn.com.greencraze.auth.exception;

public class BlockedUserException extends RuntimeException {

    public BlockedUserException() {
        super("User account is blocked");
    }

}
