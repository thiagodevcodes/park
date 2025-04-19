CREATE TABLE tb_vehicle (
    id BIGSERIAL PRIMARY KEY,
    plate VARCHAR(30) NOT NULL UNIQUE,
    make VARCHAR(30),
    model VARCHAR(30) NOT NULL,
    monthly_vehicle BOOLEAN NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    CONSTRAINT UK_vehiclePlate UNIQUE (plate)
);
