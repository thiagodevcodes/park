CREATE TABLE tb_person (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(30),
    email VARCHAR(50),
    phone VARCHAR(20),
    cpf VARCHAR(20), 
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    CONSTRAINT UK_personCpf UNIQUE (cpf),
    CONSTRAINT UK_personEmail UNIQUE (email),
    CONSTRAINT UK_personPhone UNIQUE (phone)
);