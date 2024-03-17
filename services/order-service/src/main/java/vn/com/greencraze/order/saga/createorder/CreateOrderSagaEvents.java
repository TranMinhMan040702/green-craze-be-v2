package vn.com.greencraze.order.saga.createorder;

public enum CreateOrderSagaEvents {
    REDUCE_STOCK,
    CANCEL_STOCK,
    CREATE_DOCKET,
    CANCEL_DOCKET,
    REVERT_STOCK,
    CLEAR_CART,
    CANCEL_CART,
    REVERT_DOCKET,
    COMPLETE_ORDER,
    CANCEL_ORDER
}
