-- V3__init_admin.sql
INSERT INTO users (id, email, password, name, role, is_active, created_at, updated_at)
VALUES (205820041223081984,
        'admin123@gmail.com',
        '$2a$10$qGwwz5fOZgtQuqsn8YsgBuu0Nxa5wMuwZeb/AJgB73cikku..Utz.',
        '관리자',
        'ADMIN',
        true,
        NOW(),
        NOW());