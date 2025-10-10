-- Crear tabla para métricas de usuario
CREATE TABLE metrica_usuario (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    tipo_metrica_id BIGINT NOT NULL,
    valor DECIMAL(15,2) NOT NULL,
    fecha_metrica DATETIME NOT NULL,
    periodo_mes INT,
    periodo_anio INT,
    metadatos TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at DATETIME,
    FOREIGN KEY (usuario_id) REFERENCES usuario(id),
    FOREIGN KEY (tipo_metrica_id) REFERENCES tipo_metrica(id),
    INDEX idx_usuario_metrica (usuario_id, tipo_metrica_id),
    INDEX idx_fecha_metrica (fecha_metrica),
    INDEX idx_periodo (periodo_anio, periodo_mes)
);

-- Crear tabla para visualizaciones de perfil
CREATE TABLE visualizacion_perfil (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_visitado_id BIGINT NOT NULL,
    usuario_visitante_id BIGINT,
    fecha_visualizacion DATETIME NOT NULL,
    direccion_ip VARCHAR(45),
    user_agent TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at DATETIME,
    FOREIGN KEY (usuario_visitado_id) REFERENCES usuario(id),
    FOREIGN KEY (usuario_visitante_id) REFERENCES usuario(id),
    INDEX idx_usuario_visitado (usuario_visitado_id),
    INDEX idx_fecha_visualizacion (fecha_visualizacion),
    INDEX idx_usuario_visitante (usuario_visitante_id)
);

-- Insertar tipos de métricas básicas
INSERT INTO tipo_metrica (nombre, descripcion, unidad_medida, activo, created_at, updated_at) VALUES
('VISUALIZACIONES_PERFIL', 'Cantidad de veces que se visualizó el perfil del usuario', 'visualizaciones', 1, NOW(), NOW()),
('USUARIOS_INTERESADOS', 'Número de usuarios que marcaron interés en eventos del usuario', 'usuarios', 1, NOW(), NOW()),
('SEGUIDORES_NUEVOS', 'Cantidad de nuevos seguidores obtenidos', 'seguidores', 1, NOW(), NOW()),
('EVENTOS_CREADOS', 'Cantidad de eventos creados por el usuario', 'eventos', 1, NOW(), NOW()),
('INTERACCIONES_TOTALES', 'Total de interacciones recibidas (visualizaciones + seguidores + intereses)', 'interacciones', 1, NOW(), NOW());