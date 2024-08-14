CREATE TABLE tb_users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(30) NOT NULL,
    password VARCHAR(255) NOT NULL,
    id_role BIGINT NOT NULL,
    id_person BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    CONSTRAINT FK_userPerson FOREIGN KEY (id_person) REFERENCES tb_person (id),
    CONSTRAINT FK_userRole FOREIGN KEY (id_role) REFERENCES tb_roles (id)
);
