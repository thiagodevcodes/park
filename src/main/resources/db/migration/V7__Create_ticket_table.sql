CREATE TABLE ticket (
    id INTEGER AUTO_INCREMENT NOT NULL,
    id_customer_vehicle INTEGER NOT NULL,
    id_vacancy INTEGER NOT NULL,
    entry_time DATETIME NOT NULL,
    exit_time DATETIME NULL,
    is_active BOOLEAN NOT NULL,
    total_price FLOAT NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    CONSTRAINT PK_ticket PRIMARY KEY (id),
    CONSTRAINT FK_ticketVacancy FOREIGN KEY (id_vacancy) REFERENCES vacancy (id),
    CONSTRAINT FK_ticket_customerVehicle FOREIGN KEY (id_customer_vehicle) REFERENCES vehicle_customer (id)
);