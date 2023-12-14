package vn.com.greencraze.order.enumeration;

import java.util.Arrays;
import java.util.List;

public enum PaymentCode {
    COD,
    PAYPAL;

    public static List<PaymentCode> Code() {
        return Arrays.asList(values());
    }
}
