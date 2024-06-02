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
    CONSTRAINT UK_personEmail UNIQUE  (email)
);