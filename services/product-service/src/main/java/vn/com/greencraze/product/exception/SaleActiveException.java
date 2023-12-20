package vn.com.greencraze.product.exception;

public class SaleActiveException extends RuntimeException {

    public SaleActiveException(String message) {
        super(message);
    }

    public SaleActiveException() {
        super("Sale is active");
    }

}
