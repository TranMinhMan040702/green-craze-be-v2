# Role
CREATE TABLE `role`
(
    id         VARCHAR(255) NOT NULL,
    created_at datetime     NOT NULL,
    updated_at datetime     NOT NULL,
    created_by VARCHAR(255) NULL,
    updated_by VARCHAR(255) NULL,
    name       VARCHAR(255) NOT NULL,
    code       VARCHAR(255) NOT NULL,
    CONSTRAINT pk_role PRIMARY KEY (id)
);

ALTER TABLE `role`
    ADD CONSTRAINT uc_role_code UNIQUE (code);

# Identity
CREATE TABLE identity
(
    id         VARCHAR(255) NOT NULL,
    created_at datetime     NOT NULL,
    updated_at datetime     NOT NULL,
    created_by VARCHAR(255) NULL,
    updated_by VARCHAR(255) NULL,
    username   VARCHAR(255) NOT NULL,
    password   VARCHAR(255) NOT NULL,
    status     VARCHAR(255) NOT NULL,
    CONSTRAINT pk_identity PRIMARY KEY (id)
);

CREATE TABLE identity_role
(
    identity_id VARCHAR(255) NOT NULL,
    role_id     VARCHAR(255) NOT NULL,
    CONSTRAINT pk_identity_role PRIMARY KEY (identity_id, role_id)
);

ALTER TABLE identity
    ADD CONSTRAINT uc_identity_username UNIQUE (username);

ALTER TABLE identity_role
    ADD CONSTRAINT fk_iderol_on_identity FOREIGN KEY (identity_id) REFERENCES identity (id);

ALTER TABLE identity_role
    ADD CONSTRAINT fk_iderol_on_role FOREIGN KEY (role_id) REFERENCES `role` (id);

# IdentityToken
CREATE TABLE identity_token
(
    id          BIGINT AUTO_INCREMENT NOT NULL,
    created_at  datetime              NOT NULL,
    updated_at  datetime              NOT NULL,
    created_by  VARCHAR(255)          NULL,
    updated_by  VARCHAR(255)          NULL,
    token       VARCHAR(255)          NULL,
    type        VARCHAR(255)          NOT NULL,
    expired_at  datetime              NULL,
    identity_id VARCHAR(255)          NOT NULL,
    CONSTRAINT pk_identity_token PRIMARY KEY (id)
);

ALTER TABLE identity_token
    ADD CONSTRAINT FK_IDENTITY_TOKEN_ON_IDENTITY FOREIGN KEY (identity_id) REFERENCES identity (id);

# View
CREATE VIEW core_auth.user_profile_view AS
SELECT id, identity_id
FROM core_user.user_profile