CREATE TABLE notification
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    created_at datetime              NOT NULL,
    updated_at datetime              NOT NULL,
    created_by VARCHAR(255)          NULL,
    updated_by VARCHAR(255)          NULL,
    user_id    VARCHAR(255)          NOT NULL,
    type       VARCHAR(255)          NOT NULL,
    content    VARCHAR(255)          NOT NULL,
    title      VARCHAR(255)          NOT NULL,
    anchor     VARCHAR(255)          NOT NULL,
    image      VARCHAR(255)          NOT NULL,
    status     BIT(1)                NOT NULL,
    CONSTRAINT pk_notification PRIMARY KEY (id)
);