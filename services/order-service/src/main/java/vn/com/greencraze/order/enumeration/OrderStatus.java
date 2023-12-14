package vn.com.greencraze.order.enumeration;

import java.util.Arrays;
import java.util.List;

public enum OrderStatus {
    NOT_PROCESSED,
    PROCESSING,
    SHIPPED,
    DELIVERED,
    CANCELLED;

    public static List<OrderStatus> Status() {
        return Arrays.asList(values());
    }
}