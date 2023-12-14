# Docket
CREATE TABLE docket
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    created_at datetime              NOT NULL,
    updated_at datetime              NOT NULL,
    created_by VARCHAR(255)          NULL,
    updated_by VARCHAR(255)          NULL,
    order_id   BIGINT                NULL,
    product_id BIGINT                NOT NULL,
    quantity   BIGINT                NOT NULL,
    type       VARCHAR(255)          NOT NULL,
    code       VARCHAR(255)          NOT NULL,
    note       VARCHAR(255)          NOT NULL,
    CONSTRAINT pk_docket PRIMARY KEY (id)
);

ALTER TABLE docket
    ADD CONSTRAINT uc_docket_code UNIQUE (code);