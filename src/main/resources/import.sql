-- Roles base de la plataforma Finexia (HU 19 - Compartir acceso familiar).
-- ROLE_ADMIN -> responsable de finanzas (CRUD financiero).
-- ROLE_USER  -> familiar con acceso de consulta (solo lectura).
INSERT INTO roles (name) VALUES ('ROLE_USER');
INSERT INTO roles (name) VALUES ('ROLE_ADMIN');

-- Hash BCrypt de "password" para pruebas iniciales (HU 01 - Registro).
INSERT INTO users(username, password) VALUES ('user1','$2a$12$1k34YdrmxBkVborQvZLh2OUvX1S80GVVQjZJ5H55y1eez7XV.nV06');
INSERT INTO users(username, password) VALUES ('admin','$2a$12$1k34YdrmxBkVborQvZLh2OUvX1S80GVVQjZJ5H55y1eez7XV.nV06');
INSERT INTO user_roles (user_id, role_id) VALUES (1, 1); -- user1 con ROLE_USER (familiar consulta)
INSERT INTO user_roles (user_id, role_id) VALUES (2, 2); -- admin con ROLE_ADMIN (responsable finanzas)
