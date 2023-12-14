package vn.com.greencraze.product.exception;

public class SaleInactiveException extends RuntimeException {

    public SaleInactiveException() {
        super("Sale is unactivated");
    }

}
