# UserProfile
CREATE TABLE user_profile
(
    id          VARCHAR(255) NOT NULL,
    created_at  datetime     NOT NULL,
    updated_at  datetime     NOT NULL,
    created_by  VARCHAR(255) NULL,
    updated_by  VARCHAR(255) NULL,
    identity_id VARCHAR(255) NOT NULL,
    first_name  VARCHAR(255) NOT NULL,
    last_name   VARCHAR(255) NOT NULL,
    email       VARCHAR(255) NOT NULL,
    phone       VARCHAR(255) NULL,
    dob         datetime     NULL,
    gender      VARCHAR(255) NOT NULL,
    avatar      TEXT         NULL,
    CONSTRAINT pk_user_profile PRIMARY KEY (id)
);

ALTER TABLE user_profile
    ADD CONSTRAINT uc_user_profile_email UNIQUE (email);

ALTER TABLE user_profile
    ADD CONSTRAINT uc_user_profile_identity UNIQUE (identity_id);

ALTER TABLE user_profile
    ADD CONSTRAINT uc_user_profile_phone UNIQUE (phone);

# Staff
CREATE TABLE staff
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    created_at datetime              NOT NULL,
    updated_at datetime              NOT NULL,
    created_by VARCHAR(255)          NULL,
    updated_by VARCHAR(255)          NULL,
    type       VARCHAR(255)          NOT NULL,
    code       VARCHAR(255)          NOT NULL,
    user_id    VARCHAR(255)          NOT NULL,
    CONSTRAINT pk_staff PRIMARY KEY (id)
);

ALTER TABLE staff
    ADD CONSTRAINT uc_staff_user UNIQUE (user_id);

ALTER TABLE staff
    ADD CONSTRAINT FK_STAFF_ON_USER FOREIGN KEY (user_id) REFERENCES user_profile (id);

# Cart
CREATE TABLE cart
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    created_at datetime              NOT NULL,
    updated_at datetime              NOT NULL,
    created_by VARCHAR(255)          NULL,
    updated_by VARCHAR(255)          NULL,
    user_id    VARCHAR(255)          NOT NULL,
    CONSTRAINT pk_cart PRIMARY KEY (id)
);

ALTER TABLE cart
    ADD CONSTRAINT uc_cart_user UNIQUE (user_id);

ALTER TABLE cart
    ADD CONSTRAINT FK_CART_ON_USER FOREIGN KEY (user_id) REFERENCES user_profile (id);

# CartItem
CREATE TABLE cart_item
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    created_at datetime              NOT NULL,
    updated_at datetime              NOT NULL,
    created_by VARCHAR(255)          NULL,
    updated_by VARCHAR(255)          NULL,
    cart_id    BIGINT                NOT NULL,
    variant_id BIGINT                NOT NULL,
    quantity   INT                   NOT NULL,
    CONSTRAINT pk_cart_item PRIMARY KEY (id)
);

ALTER TABLE cart_item
    ADD CONSTRAINT FK_CART_ITEM_ON_CART FOREIGN KEY (cart_id) REFERENCES cart (id);

# Review
CREATE TABLE review
(
    id            BIGINT AUTO_INCREMENT NOT NULL,
    created_at    datetime              NOT NULL,
    updated_at    datetime              NOT NULL,
    created_by    VARCHAR(255)          NULL,
    updated_by    VARCHAR(255)          NULL,
    product_id    BIGINT                NOT NULL,
    order_item_id BIGINT                NOT NULL,
    title         VARCHAR(255)          NOT NULL,
    content       TEXT                  NOT NULL,
    rating        INT                   NOT NULL,
    image         TEXT                  NULL,
    reply         TEXT                  NULL,
    status        BIT(1)                NULL,
    user_id       VARCHAR(255)          NOT NULL,
    CONSTRAINT pk_review PRIMARY KEY (id)
);

ALTER TABLE review
    ADD CONSTRAINT FK_REVIEW_ON_USER FOREIGN KEY (user_id) REFERENCES user_profile (id);

# UserFollowProduct
CREATE TABLE user_follow_product
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    created_at datetime              NOT NULL,
    updated_at datetime              NOT NULL,
    created_by VARCHAR(255)          NULL,
    updated_by VARCHAR(255)          NULL,
    user_id    VARCHAR(255)          NOT NULL,
    CONSTRAINT pk_user_follow_product PRIMARY KEY (id)
);

ALTER TABLE user_follow_product
    ADD CONSTRAINT FK_USER_FOLLOW_PRODUCT_ON_USER FOREIGN KEY (user_id) REFERENCES user_profile (id);

# UserFollowPost
CREATE TABLE user_follow_post
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    created_at datetime              NOT NULL,
    updated_at datetime              NOT NULL,
    created_by VARCHAR(255)          NULL,
    updated_by VARCHAR(255)          NULL,
    user_id    VARCHAR(255)          NOT NULL,
    CONSTRAINT pk_user_follow_post PRIMARY KEY (id)
);

ALTER TABLE user_follow_post
    ADD CONSTRAINT FK_USER_FOLLOW_POST_ON_USER FOREIGN KEY (user_id) REFERENCES user_profile (id);