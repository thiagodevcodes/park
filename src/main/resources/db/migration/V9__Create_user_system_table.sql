CREATE TABLE user_system (
    id INTEGER AUTO_INCREMENT NOT NULL,
    id_person INTEGER NOT NULL,
    id_user_type INTEGER NOT NULL,
    username VARCHAR(30) NOT NULL,
    password VARCHAR(50) NOT NULL,
    is_active BOOLEAN NOT NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    CONSTRAINT PK_user PRIMARY KEY (id),
    CONSTRAINT FK_user_Person FOREIGN KEY (id_person) REFERENCES person (id),
    CONSTRAINT FK_user_UserType FOREIGN KEY (id_user_type) REFERENCES user_type (id),
    CONSTRAINT UK_username UNIQUE (username)
);