# PostCategory
CREATE TABLE post_category
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    created_at datetime              NOT NULL,
    updated_at datetime              NOT NULL,
    created_by VARCHAR(255)          NULL,
    updated_by VARCHAR(255)          NULL,
    name       VARCHAR(255)          NOT NULL,
    slug       VARCHAR(255)          NOT NULL,
    status     BIT(1)                NOT NULL,
    CONSTRAINT pk_post_category PRIMARY KEY (id)
);

ALTER TABLE post_category
    ADD CONSTRAINT uc_post_category_name UNIQUE (name);

ALTER TABLE post_category
    ADD CONSTRAINT uc_post_category_slug UNIQUE (slug);

# Post
CREATE TABLE post
(
    id               BIGINT AUTO_INCREMENT NOT NULL,
    created_at       datetime              NOT NULL,
    updated_at       datetime              NOT NULL,
    created_by       VARCHAR(255)          NULL,
    updated_by       VARCHAR(255)          NULL,
    title            VARCHAR(255)          NOT NULL,
    content          TEXT                  NOT NULL,
    like_count       INT                   NOT NULL,
    comment_count    INT                   NOT NULL,
    slug             VARCHAR(255)          NOT NULL,
    status           BIT(1)                NOT NULL,
    post_category_id BIGINT                NOT NULL,
    CONSTRAINT pk_post PRIMARY KEY (id)
);

ALTER TABLE post
    ADD CONSTRAINT uc_post_slug UNIQUE (slug);

ALTER TABLE post
    ADD CONSTRAINT FK_POST_ON_POST_CATEGORY FOREIGN KEY (post_category_id) REFERENCES post_category (id);

# PostImage
CREATE TABLE post_image
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    created_at datetime              NOT NULL,
    updated_at datetime              NOT NULL,
    created_by VARCHAR(255)          NULL,
    updated_by VARCHAR(255)          NULL,
    image      TEXT                  NOT NULL,
    post_id    BIGINT                NOT NULL,
    CONSTRAINT pk_post_image PRIMARY KEY (id)
);

ALTER TABLE post_image
    ADD CONSTRAINT FK_POST_IMAGE_ON_POST FOREIGN KEY (post_id) REFERENCES post (id);

# Comment
CREATE TABLE comment
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    created_at datetime              NOT NULL,
    updated_at datetime              NOT NULL,
    created_by VARCHAR(255)          NULL,
    updated_by VARCHAR(255)          NULL,
    user_id    BIGINT                NOT NULL,
    content    TEXT                  NOT NULL,
    media      TEXT                  NULL,
    status     BIT(1)                NOT NULL,
    post_id    BIGINT                NOT NULL,
    CONSTRAINT pk_comment PRIMARY KEY (id)
);

ALTER TABLE comment
    ADD CONSTRAINT FK_COMMENT_ON_POST FOREIGN KEY (post_id) REFERENCES post (id);
