CREATE TABLE tb_ticket (
    id BIGSERIAL PRIMARY KEY,
    id_customer_vehicle BIGINT NOT NULL,
    id_vacancy BIGINT NOT NULL,
    entry_time TIMESTAMP NOT NULL,
    exit_time TIMESTAMP NULL,
    is_active BOOLEAN NOT NULL,
    total_price FLOAT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    CONSTRAINT FK_ticketVacancy FOREIGN KEY (id_vacancy) REFERENCES tb_vacancy (id),
    CONSTRAINT FK_ticket_customerVehicle FOREIGN KEY (id_customer_vehicle) REFERENCES tb_vehicle_customer (id)
);