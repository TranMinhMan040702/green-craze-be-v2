package vn.com.greencraze.order.saga.createorder;

public enum CreateOrderSagaStates {
    ORDER_CREATED,
    STOCK_REDUCED,
    STOCK_ERROR,
    DOCKET_CREATED,
    DOCKET_ERROR,
    CART_CLEARED,
    CART_ERROR,
    STOCK_REVERTED,
    DOCKET_REVERTED,
    ORDER_COMPLETED,
    ORDER_CANCELED
}
