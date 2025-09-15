-- Migración para agregar campo tipo a VerificationToken y cambiar relación

-- Agregar columna tipo
ALTER TABLE verification_token ADD COLUMN tipo VARCHAR(50);

-- Establecer un valor por defecto para los tokens existentes (asumimos que son de verificación)
UPDATE verification_token SET tipo = 'VERIFICACION_CUENTA' WHERE tipo IS NULL;

-- Hacer que la columna sea NOT NULL
ALTER TABLE verification_token MODIFY COLUMN tipo VARCHAR(50) NOT NULL;

-- Crear índice para mejorar el rendimiento en búsquedas por usuario y tipo
CREATE INDEX idx_verification_token_usuario_tipo ON verification_token(usuario_id, tipo);
