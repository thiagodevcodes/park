CREATE TABLE tb_vehicle_customer (
    id BIGSERIAL PRIMARY KEY,
    id_customer BIGINT NOT NULL,
    id_vehicle BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    CONSTRAINT FK_vehicleCustomer_vehicle FOREIGN KEY (id_vehicle) REFERENCES tb_vehicle (id),
    CONSTRAINT FK_vehicleCustomer_customer FOREIGN KEY (id_customer) REFERENCES tb_customer (id)
);