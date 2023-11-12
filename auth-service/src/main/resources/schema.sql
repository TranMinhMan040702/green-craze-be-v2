# Role
CREATE TABLE `role`
(
    id         VARCHAR(255) NOT NULL,
    created_at datetime     NOT NULL,
    updated_at datetime     NOT NULL,
    created_by VARCHAR(255) NULL,
    updated_by VARCHAR(255) NULL,
    name       VARCHAR(255) NOT NULL,
    status     BIT(1)       NOT NULL,
    CONSTRAINT pk_role PRIMARY KEY (id)
);

ALTER TABLE `role`
    ADD CONSTRAINT uc_role_name UNIQUE (name);

# User
CREATE TABLE user
(
    id         VARCHAR(255) NOT NULL,
    created_at datetime     NOT NULL,
    updated_at datetime     NOT NULL,
    created_by VARCHAR(255) NULL,
    updated_by VARCHAR(255) NULL,
    first_name VARCHAR(255) NOT NULL,
    last_name  VARCHAR(255) NOT NULL,
    email      VARCHAR(255) NOT NULL,
    phone      VARCHAR(255) NOT NULL,
    dob        datetime     NULL,
    gender     VARCHAR(255) NOT NULL,
    avatar     TEXT         NULL,
    password   VARCHAR(255) NOT NULL,
    status     BIT(1)       NOT NULL,
    CONSTRAINT pk_user PRIMARY KEY (id)
);

CREATE TABLE user_role
(
    role_id VARCHAR(255) NOT NULL,
    user_id VARCHAR(255) NOT NULL,
    CONSTRAINT pk_user_role PRIMARY KEY (role_id, user_id)
);

ALTER TABLE user
    ADD CONSTRAINT uc_user_email UNIQUE (email);

ALTER TABLE user
    ADD CONSTRAINT uc_user_phone UNIQUE (phone);

ALTER TABLE user_role
    ADD CONSTRAINT fk_user_role_on_role FOREIGN KEY (role_id) REFERENCES `role` (id);

ALTER TABLE user_role
    ADD CONSTRAINT fk_user_role_on_user FOREIGN KEY (user_id) REFERENCES user (id);

# UserToken
CREATE TABLE user_token
(
    id         VARCHAR(255) NOT NULL,
    created_at datetime     NOT NULL,
    updated_at datetime     NOT NULL,
    created_by VARCHAR(255) NULL,
    updated_by VARCHAR(255) NULL,
    token      VARCHAR(255) NOT NULL,
    expired_at datetime     NOT NULL,
    user_id    VARCHAR(255) NOT NULL,
    CONSTRAINT pk_user_token PRIMARY KEY (id)
);

ALTER TABLE user_token
    ADD CONSTRAINT FK_USER_TOKEN_ON_USER FOREIGN KEY (user_id) REFERENCES user (id);

# Staff
CREATE TABLE staff
(
    id         VARCHAR(255) NOT NULL,
    created_at datetime     NOT NULL,
    updated_at datetime     NOT NULL,
    created_by VARCHAR(255) NULL,
    updated_by VARCHAR(255) NULL,
    type       VARCHAR(255) NOT NULL,
    code       VARCHAR(255) NOT NULL,
    user_id    VARCHAR(255) NOT NULL,
    CONSTRAINT pk_staff PRIMARY KEY (id)
);

ALTER TABLE staff
    ADD CONSTRAINT uc_staff_user UNIQUE (user_id);

ALTER TABLE staff
    ADD CONSTRAINT FK_STAFF_ON_USER FOREIGN KEY (user_id) REFERENCES user (id);