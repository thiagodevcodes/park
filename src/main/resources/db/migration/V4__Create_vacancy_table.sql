CREATE TABLE vacancy (
    id INTEGER AUTO_INCREMENT NOT NULL,
    situation BOOLEAN NOT NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    CONSTRAINT PK_vacancy PRIMARY KEY (id)
);