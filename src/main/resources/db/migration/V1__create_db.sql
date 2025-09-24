-- MySQL 8 DDL to create all project tables
-- Generated from JPA entities under src/main/java/indie/models
-- Notes:
-- - UUID ids stored as CHAR(36)
-- - LocalDateTime/Date mapped to DATETIME
-- - EnumType.STRING mapped to VARCHAR, default (ORDINAL) mapped to INT
-- - Boolean mapped to BOOLEAN

SET FOREIGN_KEY_CHECKS = 0;

-- Base-independent tables
CREATE TABLE IF NOT EXISTS `Archivo` (
  `id` CHAR(36) NOT NULL,
  `createdAt` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updatedAt` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deletedAt` DATETIME NULL,
  `nombreArchivo` VARCHAR(255) NOT NULL,
  `tipoClase` INT NOT NULL,
  `tipoArchivo` VARCHAR(255) NOT NULL,
  `urlArchivo` VARCHAR(500) NOT NULL,
  `idObjeto` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `TipoUsuario` (
  `id` CHAR(36) NOT NULL,
  `createdAt` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updatedAt` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deletedAt` DATETIME NULL,
  `nombreTipoUsuario` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `Calendario` (
  `id` CHAR(36) NOT NULL,
  `createdAt` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updatedAt` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deletedAt` DATETIME NULL,
  `zonaHoraria` VARCHAR(100),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `TipoMetrica` (
  `id` CHAR(36) NOT NULL,
  `createdAt` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updatedAt` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deletedAt` DATETIME NULL,
  `nombre` VARCHAR(255) NOT NULL,
  `descripcion` VARCHAR(500),
  `unidadMedida` VARCHAR(100) NOT NULL,
  `activo` BOOLEAN NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `TipoNotificacion` (
  `id` CHAR(36) NOT NULL,
  `createdAt` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updatedAt` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deletedAt` DATETIME NULL,
  `nombreTipoNotificacion` VARCHAR(255) NOT NULL,
  `notificarEmailTipoNotificacion` BOOLEAN NOT NULL,
  `fechaAltaTipoNotificacion` DATETIME NOT NULL,
  `fechaBajaTipoNotificacion` DATETIME NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Dependent tables
CREATE TABLE IF NOT EXISTS `SubTipoUsuario` (
  `id` CHAR(36) NOT NULL,
  `createdAt` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updatedAt` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deletedAt` DATETIME NULL,
  `nombreSubTipoUsuario` VARCHAR(255) NOT NULL,
  `tipoUsuario_id` CHAR(36) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_subtipousuario_tipousuario` (`tipoUsuario_id`),
  CONSTRAINT `fk_subtipousuario_tipousuario` FOREIGN KEY (`tipoUsuario_id`) REFERENCES `TipoUsuario` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `Usuario` (
  `id` CHAR(36) NOT NULL,
  `createdAt` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updatedAt` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deletedAt` DATETIME NULL,
  `nombreUsuario` VARCHAR(255) NOT NULL,
  `apellidoUsuario` VARCHAR(255),
  `emailUsuario` VARCHAR(255) NOT NULL,
  `username` VARCHAR(255) NOT NULL,
  `password` VARCHAR(255) NOT NULL,
  `descripcionUsuario` VARCHAR(1000),
  `fechaVerificacion` DATETIME NULL,
  `youtubeUsuario` VARCHAR(255),
  `spotifyUsuario` VARCHAR(255),
  `instagramUsuario` VARCHAR(255),
  `calendario_id` CHAR(36),
  `subTipoUsuario_id` CHAR(36),
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_usuario_calendario` (`calendario_id`),
  KEY `idx_usuario_subtipousuario` (`subTipoUsuario_id`),
  CONSTRAINT `fk_usuario_calendario` FOREIGN KEY (`calendario_id`) REFERENCES `Calendario` (`id`),
  CONSTRAINT `fk_usuario_subtipousuario` FOREIGN KEY (`subTipoUsuario_id`) REFERENCES `SubTipoUsuario` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `SeguimientoUsuario` (
  `id` CHAR(36) NOT NULL,
  `createdAt` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updatedAt` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deletedAt` DATETIME NULL,
  `bloqueado` BOOLEAN NOT NULL,
  `usuarioSeguido_id` CHAR(36) NOT NULL,
  `usuarioSeguidor_id` CHAR(36) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_seguido` (`usuarioSeguido_id`),
  KEY `idx_seguidor` (`usuarioSeguidor_id`),
  CONSTRAINT `fk_seguido_usuario` FOREIGN KEY (`usuarioSeguido_id`) REFERENCES `Usuario` (`id`),
  CONSTRAINT `fk_seguidor_usuario` FOREIGN KEY (`usuarioSeguidor_id`) REFERENCES `Usuario` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `Funcionalidad` (
  `id` CHAR(36) NOT NULL,
  `createdAt` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updatedAt` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deletedAt` DATETIME NULL,
  `nombreFuncionalidad` VARCHAR(255) NOT NULL,
  `descripcionFuncionalidad` VARCHAR(500) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

[... truncated: remaining statements identical to original ...]

SET FOREIGN_KEY_CHECKS = 1;
