# Brand
CREATE TABLE brand
(
    id            BIGINT AUTO_INCREMENT NOT NULL,
    created_at    datetime              NOT NULL,
    updated_at    datetime              NOT NULL,
    created_by    VARCHAR(255)          NULL,
    updated_by    VARCHAR(255)          NULL,
    name          VARCHAR(255)          NOT NULL,
    code          VARCHAR(255)          NOT NULL,
    `description` TEXT                  NOT NULL,
    image         TEXT                  NOT NULL,
    status        BIT(1)                NOT NULL,
    CONSTRAINT pk_brand PRIMARY KEY (id)
);

ALTER TABLE brand
    ADD CONSTRAINT uc_brand_code UNIQUE (code);

ALTER TABLE brand
    ADD CONSTRAINT uc_brand_name UNIQUE (name);

# Unit
CREATE TABLE unit
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    created_at datetime              NOT NULL,
    updated_at datetime              NOT NULL,
    created_by VARCHAR(255)          NULL,
    updated_by VARCHAR(255)          NULL,
    name       VARCHAR(255)          NOT NULL,
    status     BIT(1)                NOT NULL,
    CONSTRAINT pk_unit PRIMARY KEY (id)
);

ALTER TABLE unit
    ADD CONSTRAINT uc_unit_name UNIQUE (name);

# ProductCategory
CREATE TABLE product_category
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    created_at datetime              NOT NULL,
    updated_at datetime              NOT NULL,
    created_by VARCHAR(255)          NULL,
    updated_by VARCHAR(255)          NULL,
    name       VARCHAR(255)          NOT NULL,
    parent_id  BIGINT                NULL,
    image      TEXT                  NOT NULL,
    slug       VARCHAR(255)          NOT NULL,
    status     BIT(1)                NOT NULL,
    CONSTRAINT pk_product_category PRIMARY KEY (id)
);

ALTER TABLE product_category
    ADD CONSTRAINT uc_product_category_name UNIQUE (name);

ALTER TABLE product_category
    ADD CONSTRAINT uc_product_category_slug UNIQUE (slug);

# Sale
CREATE TABLE sale
(
    id                  BIGINT AUTO_INCREMENT NOT NULL,
    created_at          datetime              NOT NULL,
    updated_at          datetime              NOT NULL,
    created_by          VARCHAR(255)          NULL,
    updated_by          VARCHAR(255)          NULL,
    name                VARCHAR(255)          NOT NULL,
    `description`       TEXT                  NOT NULL,
    image               TEXT                  NOT NULL,
    start_date          datetime              NOT NULL,
    end_date            datetime              NOT NULL,
    promotional_percent DOUBLE                NOT NULL,
    slug                VARCHAR(255)          NOT NULL,
    status              VARCHAR(255)          NOT NULL,
    CONSTRAINT pk_sale PRIMARY KEY (id)
);

CREATE TABLE sale_product_category
(
    product_category_id BIGINT NOT NULL,
    sale_id             BIGINT NOT NULL,
    CONSTRAINT pk_sale_product_category PRIMARY KEY (product_category_id, sale_id)
);

ALTER TABLE sale
    ADD CONSTRAINT uc_sale_name UNIQUE (name);

ALTER TABLE sale
    ADD CONSTRAINT uc_sale_slug UNIQUE (slug);

ALTER TABLE sale_product_category
    ADD CONSTRAINT fk_salprocat_on_product_category FOREIGN KEY (product_category_id) REFERENCES product_category (id);

ALTER TABLE sale_product_category
    ADD CONSTRAINT fk_salprocat_on_sale FOREIGN KEY (sale_id) REFERENCES sale (id);

# Product
CREATE TABLE product
(
    id                  BIGINT AUTO_INCREMENT NOT NULL,
    created_at          datetime              NOT NULL,
    updated_at          datetime              NOT NULL,
    created_by          VARCHAR(255)          NULL,
    updated_by          VARCHAR(255)          NULL,
    name                VARCHAR(255)          NOT NULL,
    short_description   TEXT                  NOT NULL,
    `description`       TEXT                  NOT NULL,
    code                VARCHAR(255)          NOT NULL,
    quantity            BIGINT                NOT NULL,
    sold                BIGINT                NULL,
    rating              DOUBLE                NOT NULL,
    slug                VARCHAR(255)          NOT NULL,
    cost                DECIMAL               NOT NULL,
    status              VARCHAR(255)          NOT NULL,
    product_category_id BIGINT                NOT NULL,
    brand_id            BIGINT                NOT NULL,
    unit_id             BIGINT                NOT NULL,
    CONSTRAINT pk_product PRIMARY KEY (id)
);

ALTER TABLE product
    ADD CONSTRAINT uc_product_code UNIQUE (code);

ALTER TABLE product
    ADD CONSTRAINT uc_product_name UNIQUE (name);

ALTER TABLE product
    ADD CONSTRAINT uc_product_slug UNIQUE (slug);

ALTER TABLE product
    ADD CONSTRAINT FK_PRODUCT_ON_BRAND FOREIGN KEY (brand_id) REFERENCES brand (id);

ALTER TABLE product
    ADD CONSTRAINT FK_PRODUCT_ON_PRODUCT_CATEGORY FOREIGN KEY (product_category_id) REFERENCES product_category (id);

ALTER TABLE product
    ADD CONSTRAINT FK_PRODUCT_ON_UNIT FOREIGN KEY (unit_id) REFERENCES unit (id);

# ProductImage
CREATE TABLE product_image
(
    id           BIGINT AUTO_INCREMENT NOT NULL,
    created_at   datetime              NOT NULL,
    updated_at   datetime              NOT NULL,
    created_by   VARCHAR(255)          NULL,
    updated_by   VARCHAR(255)          NULL,
    image        TEXT                  NOT NULL,
    size         DOUBLE                NULL,
    content_type VARCHAR(255)          NULL,
    is_default   BIT(1)                NOT NULL,
    product_id   BIGINT                NOT NULL,
    CONSTRAINT pk_product_image PRIMARY KEY (id)
);

ALTER TABLE product_image
    ADD CONSTRAINT FK_PRODUCT_IMAGE_ON_PRODUCT FOREIGN KEY (product_id) REFERENCES product (id);

# Variant
CREATE TABLE variant
(
    id                      BIGINT AUTO_INCREMENT NOT NULL,
    created_at              datetime              NOT NULL,
    updated_at              datetime              NOT NULL,
    created_by              VARCHAR(255)          NULL,
    updated_by              VARCHAR(255)          NULL,
    name                    VARCHAR(255)          NOT NULL,
    sku                     VARCHAR(255)          NOT NULL,
    quantity                BIGINT                NOT NULL,
    item_price              DECIMAL               NOT NULL,
    total_price             DECIMAL               NOT NULL,
    promotional_item_price  DECIMAL               NULL,
    total_promotional_price DECIMAL               NULL,
    status                  VARCHAR(255)          NOT NULL,
    product_id              BIGINT                NOT NULL,
    CONSTRAINT pk_variant PRIMARY KEY (id)
);

ALTER TABLE variant
    ADD CONSTRAINT uc_variant_sku UNIQUE (sku);

ALTER TABLE variant
    ADD CONSTRAINT FK_VARIANT_ON_PRODUCT FOREIGN KEY (product_id) REFERENCES product (id);