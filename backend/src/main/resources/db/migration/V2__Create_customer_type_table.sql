CREATE TABLE tb_customer_type (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(30) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    is_active BOOLEAN
);