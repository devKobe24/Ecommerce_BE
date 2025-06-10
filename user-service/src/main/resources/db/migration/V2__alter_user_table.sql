-- V2__alter_user_table.sql
-- last_login_at 및 is_active 컬럼 추가

ALTER TABLE users
    ADD COLUMN last_login_at DATETIME DEFAULT NULL COMMENT '마지막 로그인 시간',
    ADD COLUMN is_active BOOLEAN NOT NULL DEFAULT TRUE COMMENT '활성 사용자 여부';