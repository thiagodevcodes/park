CREATE TABLE person (
    id INTEGER AUTO_INCREMENT NOT NULL,
    name VARCHAR(30) NULL,
    email VARCHAR(50) NULL,
    phone VARCHAR(20) NULL,
    cpf VARCHAR(20) NULL, 
    created_at datetime NOT NULL,
    updated_at datetime NULL,
    CONSTRAINT PK_person PRIMARY KEY (id),
    CONSTRAINT UK_personCpf UNIQUE (cpf),
    CONSTRAINT UK_personEmail UNIQUE  (email),
    CONSTRAINT UK_personPhone UNIQUE (phone)
);

CREATE TABLE customer_type (
    id INTEGER AUTO_INCREMENT NOT NULL,
    name VARCHAR(30) NOT NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    is_active BOOLEAN,
    CONSTRAINT PK_customerType PRIMARY KEY (id)
);

CREATE TABLE customer (
    id INTEGER AUTO_INCREMENT NOT NULL,
    id_person INTEGER NOT NULL,
    id_customer_type INTEGER NOT NULL,
    payment_day INTEGER NULL,
    is_active BOOLEAN NOT NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    CONSTRAINT PK_customer PRIMARY KEY (id),
    CONSTRAINT FK_customerPerson FOREIGN KEY (id_person) REFERENCES person (id),
    CONSTRAINT FK_customerCustomerType FOREIGN KEY (id_customer_type) REFERENCES customer_type (id)
);

CREATE TABLE vacancy (
    id INTEGER AUTO_INCREMENT NOT NULL,
    situation BOOLEAN NOT NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    CONSTRAINT PK_vacancy PRIMARY KEY (id)
);

CREATE TABLE vehicle (
    id INTEGER AUTO_INCREMENT NOT NULL,
    id_customer INTEGER NOT NULL,
    plate VARCHAR(30) NOT NULL,
    make VARCHAR(30) NULL,
    model VARCHAR(30) NOT NULL,
    monthly_vehicle BOOLEAN NOT NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    CONSTRAINT PK_vehicle PRIMARY KEY (id),
    CONSTRAINT UK_vehiclePlate UNIQUE (plate),
    CONSTRAINT FK_vehicleCustomer FOREIGN KEY (id_customer) REFERENCES vehicle (id)
);
CREATE TABLE ticket (
    id INTEGER AUTO_INCREMENT NOT NULL,
    id_customer INTEGER NOT NULL,
    id_vacancy INTEGER NOT NULL,
    id_vehicle INTEGER NOT NULL,
    entry_time DATETIME NOT NULL,
    exit_time DATETIME NULL,
    total_price FLOAT NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    CONSTRAINT PK_ticket PRIMARY KEY (id),
    CONSTRAINT FK_ticketVehicle FOREIGN KEY (id_vehicle) REFERENCES vehicle (id),
    CONSTRAINT FK_ticketVacancy FOREIGN KEY (id_vacancy) REFERENCES vacancy (id),
    CONSTRAINT FK_ticketCustomer FOREIGN KEY (id_customer) REFERENCES customer (id)
);

INSERT INTO vacancy(`id`, `created_at`, `situation`, `updated_at`)
VALUES(1, current_timestamp(), true,current_timestamp());

INSERT INTO vacancy(`id`, `created_at`, `situation`, `updated_at`)
VALUES(2, current_timestamp(), true,current_timestamp());

INSERT INTO vacancy(`id`, `created_at`, `situation`, `updated_at`)
VALUES(3, current_timestamp(), true,current_timestamp());

INSERT INTO vacancy(`id`, `created_at`, `situation`, `updated_at`)
VALUES(4, current_timestamp(), true,current_timestamp());

INSERT INTO vacancy(`id`, `created_at`, `situation`, `updated_at`)
VALUES(5, current_timestamp(), true,current_timestamp());

INSERT INTO vacancy(`id`, `created_at`, `situation`, `updated_at`)
VALUES(6, current_timestamp(), true,current_timestamp());

INSERT INTO vacancy(`id`, `created_at`, `situation`, `updated_at`)
VALUES(7, current_timestamp(), true,current_timestamp());

INSERT INTO vacancy(`id`, `created_at`, `situation`, `updated_at`)
VALUES(8, current_timestamp(), true,current_timestamp());

INSERT INTO vacancy(`id`, `created_at`, `situation`, `updated_at`)
VALUES(9, current_timestamp(), true,current_timestamp());

INSERT INTO vacancy(`id`, `created_at`, `situation`, `updated_at`)
VALUES(10, current_timestamp(), true,current_timestamp());

INSERT INTO vacancy(`id`, `created_at`, `situation`, `updated_at`)
VALUES(11, current_timestamp(), true,current_timestamp());

INSERT INTO vacancy(`id`, `created_at`, `situation`, `updated_at`)
VALUES(12, current_timestamp(), true,current_timestamp());

INSERT INTO customer_type(`id`, `created_at`, `is_active`, `name`, `updated_at`)
VALUES(1, current_timestamp(), true, 'Rotativo', current_timestamp());

INSERT INTO customer_type(`id`, `created_at`, `is_active`, `name`, `updated_at`)
VALUES(2, current_timestamp(), true, 'Mensalista', current_timestamp());

