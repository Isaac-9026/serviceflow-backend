-- Corrige el hash de la contraseña admin123/password123 que era incorrecto en las migraciones previas
UPDATE usuarios 
SET password = '$2a$10$VwmHk9lm4GuRlcJMUG.DkeCTbeeLj7LJKIiAzRy3bw.hQUFb7c6.C'
WHERE email = 'admin@serviceflow.com';

--Si  YA había otros usuarios con el hash malo, los corregimos también
UPDATE usuarios 
SET password = '$2a$10$VwmHk9lm4GuRlcJMUG.DkeCTbeeLj7LJKIiAzRy3bw.hQUFb7c6.C'
WHERE password = '$2a$10$wT/t/X45/E1/qKx1YxT.oO5J92j7t3xJ5r1t925q29Xv7O5W3u1hS';
