-- Actualizar contraseñas existentes a un hash BCrypt genérico ('password123')
-- Esto es solo para entorno de desarrollo para evitar que el login falle con usuarios antiguos
-- El hash corresponde a la contraseña "password123" generada con BCrypt
UPDATE usuarios 
SET password = '$2a$10$wT/t/X45/E1/qKx1YxT.oO5J92j7t3xJ5r1t925q29Xv7O5W3u1hS'
WHERE password NOT LIKE '$2a$%';
