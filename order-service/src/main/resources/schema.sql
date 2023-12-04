# Delivery
CREATE TABLE delivery
(
    id            BIGINT AUTO_INCREMENT NOT NULL,
    created_at    datetime              NOT NULL,
    updated_at    datetime              NOT NULL,
    created_by    VARCHAR(255)          NULL,
    updated_by    VARCHAR(255)          NULL,
    name          VARCHAR(255)          NOT NULL,
    `description` VARCHAR(255)          NOT NULL,
    price         DECIMAL               NOT NULL,
    image         TEXT                  NOT NULL,
    status        BIT(1)                NOT NULL,
    CONSTRAINT pk_delivery PRIMARY KEY (id)
);

ALTER TABLE delivery
    ADD CONSTRAINT uc_delivery_name UNIQUE (name);

# PaymentMethod
CREATE TABLE payment_method
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    created_at datetime              NOT NULL,
    updated_at datetime              NOT NULL,
    created_by VARCHAR(255)          NULL,
    updated_by VARCHAR(255)          NULL,
    name       VARCHAR(255)          NOT NULL,
    code       VARCHAR(255)          NOT NULL,
    image      TEXT                  NOT NULL,
    status     BIT(1)                NOT NULL,
    CONSTRAINT pk_payment_method PRIMARY KEY (id)
);

ALTER TABLE payment_method
    ADD CONSTRAINT uc_payment_method_code UNIQUE (code);

ALTER TABLE payment_method
    ADD CONSTRAINT uc_payment_method_name UNIQUE (name);

# OrderCancelReason
CREATE TABLE order_cancel_reason
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    created_at datetime              NOT NULL,
    updated_at datetime              NOT NULL,
    created_by VARCHAR(255)          NULL,
    updated_by VARCHAR(255)          NULL,
    name       VARCHAR(255)          NOT NULL,
    note       TEXT                  NOT NULL,
    status     BIT(1)                NOT NULL,
    CONSTRAINT pk_order_cancel_reason PRIMARY KEY (id)
);

ALTER TABLE order_cancel_reason
    ADD CONSTRAINT uc_order_cancel_reason_name UNIQUE (name);

# Order
CREATE TABLE `order`
(
    id                     BIGINT AUTO_INCREMENT NOT NULL,
    created_at             datetime              NOT NULL,
    updated_at             datetime              NOT NULL,
    created_by             VARCHAR(255)          NULL,
    updated_by             VARCHAR(255)          NULL,
    user_id                VARCHAR(255)          NOT NULL,
    address_id             BIGINT                NOT NULL,
    total_amount           DECIMAL               NOT NULL,
    order_cancel_reason_id BIGINT                NULL,
    other_cancel_reason    TEXT                  NULL,
    note                   TEXT                  NULL,
    code                   VARCHAR(255)          NOT NULL,
    delivery_method        VARCHAR(255)          NOT NULL,
    shipping_cost          DECIMAL               NOT NULL,
    payment_status         BIT(1)                NOT NULL,
    tax                    DOUBLE                NOT NULL,
    status                 VARCHAR(255)          NOT NULL,
    CONSTRAINT pk_order PRIMARY KEY (id)
);

ALTER TABLE `order`
    ADD CONSTRAINT uc_order_code UNIQUE (code);

ALTER TABLE `order`
    ADD CONSTRAINT FK_ORDER_ON_ORDER_CANCEL_REASON FOREIGN KEY (order_cancel_reason_id) REFERENCES order_cancel_reason (id);

# OrderItem
CREATE TABLE order_item
(
    id          BIGINT AUTO_INCREMENT NOT NULL,
    created_at  datetime              NOT NULL,
    updated_at  datetime              NOT NULL,
    created_by  VARCHAR(255)          NULL,
    updated_by  VARCHAR(255)          NULL,
    variant_id  BIGINT                NOT NULL,
    unit_price  DECIMAL               NOT NULL,
    quantity    INT                   NOT NULL,
    total_price DECIMAL               NOT NULL,
    order_id    BIGINT                NOT NULL,
    CONSTRAINT pk_order_item PRIMARY KEY (id)
);

ALTER TABLE order_item
    ADD CONSTRAINT FK_ORDER_ITEM_ON_ORDER FOREIGN KEY (order_id) REFERENCES `order` (id);

# Transaction
CREATE TABLE transaction
(
    id                  BIGINT AUTO_INCREMENT NOT NULL,
    created_at          datetime              NOT NULL,
    updated_at          datetime              NOT NULL,
    created_by          VARCHAR(255)          NULL,
    updated_by          VARCHAR(255)          NULL,
    payment_method      VARCHAR(255)          NOT NULL,
    paid_at             datetime              NOT NULL,
    total_pay           DECIMAL               NOT NULL,
    completed_at        datetime              NOT NULL,
    paypal_order_id     VARCHAR(255)          NOT NULL,
    paypal_order_status VARCHAR(255)          NOT NULL,
    order_id            BIGINT                NOT NULL,
    CONSTRAINT pk_transaction PRIMARY KEY (id)
);

ALTER TABLE transaction
    ADD CONSTRAINT FK_TRANSACTION_ON_ORDER FOREIGN KEY (order_id) REFERENCES `order` (id);