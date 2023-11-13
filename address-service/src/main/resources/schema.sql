# Province
CREATE TABLE province
(
    id   BIGINT AUTO_INCREMENT NOT NULL,
    name VARCHAR(255)          NOT NULL,
    code VARCHAR(255)          NOT NULL,
    CONSTRAINT pk_province PRIMARY KEY (id)
);

ALTER TABLE province
    ADD CONSTRAINT uc_province_code UNIQUE (code);

ALTER TABLE province
    ADD CONSTRAINT uc_province_name UNIQUE (name);

# District
CREATE TABLE district
(
    id          BIGINT AUTO_INCREMENT NOT NULL,
    name        VARCHAR(255)          NOT NULL,
    code        VARCHAR(255)          NOT NULL,
    province_id BIGINT                NOT NULL,
    CONSTRAINT pk_district PRIMARY KEY (id)
);

ALTER TABLE district
    ADD CONSTRAINT uc_district_code UNIQUE (code);

ALTER TABLE district
    ADD CONSTRAINT uc_district_name UNIQUE (name);

ALTER TABLE district
    ADD CONSTRAINT FK_DISTRICT_ON_PROVINCE FOREIGN KEY (province_id) REFERENCES province (id);

# Ward
CREATE TABLE ward
(
    id          BIGINT AUTO_INCREMENT NOT NULL,
    name        VARCHAR(255)          NOT NULL,
    code        VARCHAR(255)          NOT NULL,
    district_id BIGINT                NOT NULL,
    CONSTRAINT pk_ward PRIMARY KEY (id)
);

ALTER TABLE ward
    ADD CONSTRAINT uc_ward_code UNIQUE (code);

ALTER TABLE ward
    ADD CONSTRAINT uc_ward_name UNIQUE (name);

ALTER TABLE ward
    ADD CONSTRAINT FK_WARD_ON_DISTRICT FOREIGN KEY (district_id) REFERENCES district (id);

# Address
CREATE TABLE address
(
    id          BIGINT AUTO_INCREMENT NOT NULL,
    created_at  datetime              NOT NULL,
    updated_at  datetime              NOT NULL,
    created_by  VARCHAR(255)          NULL,
    updated_by  VARCHAR(255)          NULL,
    user_id     BIGINT                NOT NULL,
    province_id BIGINT                NOT NULL,
    district_id BIGINT                NOT NULL,
    ward_id     BIGINT                NOT NULL,
    street      VARCHAR(255)          NOT NULL,
    receiver    VARCHAR(255)          NOT NULL,
    email       VARCHAR(255)          NULL,
    phone       VARCHAR(255)          NOT NULL,
    is_default  BIT(1)                NOT NULL,
    CONSTRAINT pk_address PRIMARY KEY (id)
);

ALTER TABLE address
    ADD CONSTRAINT FK_ADDRESS_ON_DISTRICT FOREIGN KEY (district_id) REFERENCES district (id);

ALTER TABLE address
    ADD CONSTRAINT FK_ADDRESS_ON_PROVINCE FOREIGN KEY (province_id) REFERENCES province (id);

ALTER TABLE address
    ADD CONSTRAINT FK_ADDRESS_ON_WARD FOREIGN KEY (ward_id) REFERENCES ward (id);
