CREATE TABLE vehicle (
    id INTEGER AUTO_INCREMENT NOT NULL,
    plate VARCHAR(30) NOT NULL,
    make VARCHAR(30) NULL,
    model VARCHAR(30) NOT NULL,
    monthly_vehicle BOOLEAN NOT NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    CONSTRAINT PK_vehicle PRIMARY KEY (id),
    CONSTRAINT UK_vehiclePlate UNIQUE (plate)
);