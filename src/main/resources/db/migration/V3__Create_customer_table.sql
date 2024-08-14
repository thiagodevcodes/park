CREATE TABLE tb_customer (
    id BIGSERIAL PRIMARY KEY,
    id_person BIGINT NOT NULL,
    id_customer_type BIGINT NOT NULL,
    payment_day INTEGER,
    is_active BOOLEAN NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    CONSTRAINT FK_customer_person FOREIGN KEY (id_person) REFERENCES tb_person (id),
    CONSTRAINT FK_customer_customerType FOREIGN KEY (id_customer_type) REFERENCES tb_customer_type (id)
);
