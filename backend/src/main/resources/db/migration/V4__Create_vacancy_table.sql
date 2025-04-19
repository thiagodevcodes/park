CREATE TABLE tb_vacancy (
    id BIGSERIAL PRIMARY KEY,
    situation BOOLEAN NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);