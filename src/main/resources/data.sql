
INSERT INTO vacancy(`id`, `created_at`, `situation`, `updated_at`)
VALUES(1, current_timestamp(), true,current_timestamp());

INSERT INTO person (`id`, `name`, `cpf`, `email`, `phone`, `created_at`, `updated_at`)
VALUES (3461, 'Thiago', '08516996506', 'thiago@gmail.com', '79988674823', current_timestamp(), current_timestamp());

INSERT INTO user_system(`id`, `created_at`, `updated_at`, `username`, `password`, `is_active`, `id_person`, `role`)
VALUES (3461, current_timestamp(), current_timestamp(), 'thiago', '$2a$10$mhOFFm1tLvE0Mp4NJk4QmOHyUAfy1RhE0HBXXNvFKf.MKyYlmqIqS', true, 3461, 0);

INSERT INTO vacancy(`id`, `created_at`, `situation`, `updated_at`)
VALUES(2, current_timestamp(), true,current_timestamp());

INSERT INTO vacancy(`id`, `created_at`, `situation`, `updated_at`)
VALUES(3, current_timestamp(), true,current_timestamp());

INSERT INTO vacancy(`id`, `created_at`, `situation`, `updated_at`)
VALUES(4, current_timestamp(), true,current_timestamp());

INSERT INTO vacancy(`id`, `created_at`, `situation`, `updated_at`)
VALUES(5, current_timestamp(), true,current_timestamp());

INSERT INTO vacancy(`id`, `created_at`, `situation`, `updated_at`)
VALUES(6, current_timestamp(), true,current_timestamp());

INSERT INTO vacancy(`id`, `created_at`, `situation`, `updated_at`)
VALUES(7, current_timestamp(), true,current_timestamp());

INSERT INTO vacancy(`id`, `created_at`, `situation`, `updated_at`)
VALUES(8, current_timestamp(), true,current_timestamp());

INSERT INTO vacancy(`id`, `created_at`, `situation`, `updated_at`)
VALUES(9, current_timestamp(), true,current_timestamp());

INSERT INTO vacancy(`id`, `created_at`, `situation`, `updated_at`)
VALUES(10, current_timestamp(), true,current_timestamp());

INSERT INTO vacancy(`id`, `created_at`, `situation`, `updated_at`)
VALUES(11, current_timestamp(), true,current_timestamp());

INSERT INTO vacancy(`id`, `created_at`, `situation`, `updated_at`)
VALUES(12, current_timestamp(), true,current_timestamp());

INSERT INTO customer_type(`id`, `created_at`, `is_active`, `name`, `updated_at`)
VALUES(1, current_timestamp(), true, 'Rotativo', current_timestamp());

INSERT INTO customer_type(`id`, `created_at`, `is_active`, `name`, `updated_at`)
VALUES(2, current_timestamp(), true, 'Mensalista', current_timestamp());

INSERT INTO user_type(`id`, `created_at`, `is_active`, `name`, `updated_at`)
VALUES(1, current_timestamp(), true, 'Admin', current_timestamp());

INSERT INTO user_type(`id`, `created_at`, `is_active`, `name`, `updated_at`)
VALUES(2, current_timestamp(), true, 'Default', current_timestamp());
