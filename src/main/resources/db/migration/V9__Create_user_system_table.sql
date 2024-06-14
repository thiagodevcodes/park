CREATE TABLE user_system (
    id INTEGER AUTO_INCREMENT NOT NULL,
    id_person INTEGER NOT NULL,
    username VARCHAR(30) NOT NULL,
    password VARCHAR(255) NOT NULL,
    is_active BOOLEAN NOT NULL,
    role TINYINT NOT NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    CONSTRAINT PK_user PRIMARY KEY (id),
    CONSTRAINT FK_user_Person FOREIGN KEY (id_person) REFERENCES person (id),
    CONSTRAINT UK_username UNIQUE (username)
);