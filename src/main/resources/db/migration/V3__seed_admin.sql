-- Insertar un usuario ADMIN por defecto para poder iniciar sesión la primera vez
-- El password es 'admin123' (hash BCrypt generado previamente)
INSERT INTO usuarios (email, password, nombre, apellido, rol, activo)
VALUES ('admin@serviceflow.com', '$2a$10$wT/t/X45/E1/qKx1YxT.oO5J92j7t3xJ5r1t925q29Xv7O5W3u1hS', 'Admin', 'Sistema', 'ADMIN', true)
ON CONFLICT (email) DO NOTHING;
