-- V5__init_backup_admin.sql
INSERT INTO users (id, email, password, name, role, is_active, created_at, updated_at)
SELECT 206162858885648384,
       'backupAdmin456@gmail.com',
       '$2a$10$tqvmQS.1irYp9pZnsX4sp.6Pv48ZEvFX.ZVY9oI5pSE0wBr8OkZrG', -- 🔐 원문 비밀번호: adminPassword1234 / 📌 BCrypt 해시: $2a$10$tqvmQS.1irYp9pZnsX4sp.6Pv48ZEvFX.ZVY9oI5pSE0wBr8OkZrG
       '백업관리자2',
       'ADMIN',
       true,
       NOW(),
       NOW()
    WHERE NOT EXISTS (
    SELECT 1 FROM users WHERE id = 206162858885648384
);