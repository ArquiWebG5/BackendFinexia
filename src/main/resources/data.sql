-- noinspection SqlNoDataSourceInspection,SqlResolveForFile

-- Roles base de la plataforma Finexia (HU 19 - Compartir acceso familiar).
-- ROLE_ADMIN -> responsable de finanzas (CRUD financiero).
-- ROLE_USER  -> familiar con acceso de consulta (solo lectura).
INSERT INTO roles (name) VALUES ('ROLE_USER') ON CONFLICT (name) DO NOTHING;
INSERT INTO roles (name) VALUES ('ROLE_ADMIN') ON CONFLICT (name) DO NOTHING;

-- Hash BCrypt de "password" para pruebas iniciales (HU 01 - Registro).
INSERT INTO usuario(nombre, apellido, correo, plan, idioma, moneda_preferida, tema_ui, fecha_registro)
VALUES ('Usuario', 'Consulta', 'user1@finexia.local', 'FAMILIAR', 'es', 'PEN', 'light', CURRENT_DATE)
ON CONFLICT (correo) DO NOTHING;

INSERT INTO usuario(nombre, apellido, correo, plan, idioma, moneda_preferida, tema_ui, fecha_registro)
VALUES ('Admin', 'Finanzas', 'admin@finexia.local', 'RESPONSABLE', 'es', 'PEN', 'light', CURRENT_DATE)
ON CONFLICT (correo) DO NOTHING;

INSERT INTO users(username, password, usuario_id)
VALUES (
    'user1',
    '$2a$10$iYHJ5Fj6VhpoEO745IiLfORNXnThrE8EmAX9hFlSuiqoKLiXggVTy',
    (SELECT id_usuario FROM usuario WHERE correo = 'user1@finexia.local')
)
ON CONFLICT (username) DO NOTHING;

INSERT INTO users(username, password, usuario_id)
VALUES (
    'admin',
    '$2a$10$iYHJ5Fj6VhpoEO745IiLfORNXnThrE8EmAX9hFlSuiqoKLiXggVTy',
    (SELECT id_usuario FROM usuario WHERE correo = 'admin@finexia.local')
)
ON CONFLICT (username) DO NOTHING;

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id
FROM users u, roles r
WHERE u.username = 'user1'
AND r.name = 'ROLE_USER'
AND NOT EXISTS (
    SELECT 1 FROM user_roles ur WHERE ur.user_id = u.id AND ur.role_id = r.id
);

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id
FROM users u, roles r
WHERE u.username = 'admin'
AND r.name = 'ROLE_ADMIN'
AND NOT EXISTS (
    SELECT 1 FROM user_roles ur WHERE ur.user_id = u.id AND ur.role_id = r.id
);
