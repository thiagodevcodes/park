CREATE TABLE vehicle_customer (
    id INTEGER AUTO_INCREMENT NOT NULL,
    id_customer INTEGER NOT NULL,
    id_vehicle INTEGER NOT NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    CONSTRAINT PK_customer_vehicle PRIMARY KEY (id),
    CONSTRAINT FK_vehicleCustomer_vehicle FOREIGN KEY (id_vehicle) REFERENCES vehicle (id),
    CONSTRAINT FK_vehicleCustomer_customer FOREIGN KEY (id_customer) REFERENCES customer (id)
);