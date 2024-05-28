CREATE TABLE user_type (
    id INTEGER AUTO_INCREMENT NOT NULL,
    name VARCHAR(50) NOT NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    is_active BOOLEAN,
    CONSTRAINT PK_userType PRIMARY KEY (id)
);