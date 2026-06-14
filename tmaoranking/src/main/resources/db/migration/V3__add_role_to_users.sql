ALTER TABLE tb_users ADD COLUMN role VARCHAR(20) NOT NULL DEFAULT 'USER';

INSERT INTO tb_users (name, email, password, role)
VALUES (
           'Admin',
           'admin@tmaoranking.com',
           '$2a$10$q3EZgkFI42Kc8v3k/MGiW.4MVK7me42fXTOWwwpvLv1zESOH7ETfi',
           'ADMIN'
       );