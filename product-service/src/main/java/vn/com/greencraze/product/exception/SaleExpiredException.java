package vn.com.greencraze.product.exception;

public class SaleExpiredException extends RuntimeException {

    public SaleExpiredException() {
        super("Sale is expired");
    }

}
