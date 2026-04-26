CREATE TABLE IF NOT EXISTS category
(
    id          INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    category_name        VARCHAR(255) NOT NULL UNIQUE CHECK (TRIM(category_name) <> ''),
    description TEXT,
    created_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by  VARCHAR(50),
    updated_by  VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS size
(
    category_size VARCHAR(255)   NOT NULL,
    category_id   INT            NOT NULL,
    price         DECIMAL(10, 2) NOT NULL,
    created_at    TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by    VARCHAR(50),
    updated_by    VARCHAR(50),
    PRIMARY KEY (category_size, category_id),
    FOREIGN KEY (category_id) REFERENCES category (id),
    CHECK ( price >= 1000 )
);

CREATE TABLE IF NOT EXISTS product
(
    id          INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    name        VARCHAR(255) NOT NULL,
    description TEXT,
    created_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by  VARCHAR(50),
    updated_by  VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS product_size
(
    product_id    INT          NOT NULL,
    category_size VARCHAR(255) NOT NULL,
    category_id   INT          NOT NULL,
    PRIMARY KEY (product_id, category_size, category_id),
    FOREIGN KEY (product_id) REFERENCES product (id) ON DELETE CASCADE,
    FOREIGN KEY (category_size, category_id) REFERENCES size (category_size, category_id)
);