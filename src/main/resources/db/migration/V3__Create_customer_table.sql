CREATE TABLE customer (
    id INTEGER AUTO_INCREMENT NOT NULL,
    id_person INTEGER NOT NULL,
    id_customer_type INTEGER NOT NULL,
    payment_day INTEGER NULL,
    is_active BOOLEAN NOT NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    CONSTRAINT PK_customer PRIMARY KEY (id),
    CONSTRAINT FK_customer_person FOREIGN KEY (id_person) REFERENCES person (id),
    CONSTRAINT FK_customer_customerType FOREIGN KEY (id_customer_type) REFERENCES customer_type (id)
);