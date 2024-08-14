CREATE TABLE tb_roles (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(30),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    CONSTRAINT UK_roleName UNIQUE (name)
);