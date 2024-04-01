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

CREATE TABLE room
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    created_at datetime              NOT NULL,
    updated_at datetime              NOT NULL,
    created_by VARCHAR(255)          NULL,
    updated_by VARCHAR(255)          NULL,
    user_id    VARCHAR(255)          NOT NULL,
    name       VARCHAR(255)          NOT NULL,
    CONSTRAINT pk_room PRIMARY KEY (id)
);

CREATE TABLE message
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    created_at datetime              NOT NULL,
    updated_at datetime              NOT NULL,
    created_by VARCHAR(255)          NULL,
    updated_by VARCHAR(255)          NULL,
    user_id    VARCHAR(255)          NOT NULL,
    room_id    BIGINT                NOT NULL,
    image      TEXT                  NULL,
    status     BIT(1)                NOT NULL,
    content    TEXT                  NULL,
    CONSTRAINT pk_message PRIMARY KEY (id)
);

ALTER TABLE message
    ADD CONSTRAINT FK_MESSAGE_ON_ROOM FOREIGN KEY (room_id) REFERENCES room (id);