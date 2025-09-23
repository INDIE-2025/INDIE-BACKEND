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

CREATE TABLE IF NOT EXISTS `FuncionalidadRol` (
  `id` CHAR(36) NOT NULL,
  `createdAt` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updatedAt` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deletedAt` DATETIME NULL,
  `funcionalidad_id` CHAR(36) NOT NULL,
  `tipoUsuario_id` CHAR(36) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_funcrol_func` (`funcionalidad_id`),
  KEY `idx_funcrol_tipo` (`tipoUsuario_id`),
  CONSTRAINT `fk_funcrol_func` FOREIGN KEY (`funcionalidad_id`) REFERENCES `Funcionalidad` (`id`),
  CONSTRAINT `fk_funcrol_tipo` FOREIGN KEY (`tipoUsuario_id`) REFERENCES `TipoUsuario` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `VerificationToken` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `token` VARCHAR(255),
  `usuario_id` CHAR(36),
  `expiryDate` DATETIME,
  `tipo` VARCHAR(50),
  PRIMARY KEY (`id`),
  KEY `idx_veriftoken_usuario` (`usuario_id`),
  CONSTRAINT `fk_veriftoken_usuario` FOREIGN KEY (`usuario_id`) REFERENCES `Usuario` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `Evento` (
  `id` CHAR(36) NOT NULL,
  `createdAt` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updatedAt` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deletedAt` DATETIME NULL,
  `idUsuario_id` CHAR(36) NOT NULL,
  `descripcionEvento` VARCHAR(1000),
  `fechaHoraEvento` DATETIME,
  `tituloEvento` VARCHAR(255) NOT NULL,
  `ubicacionEvento` VARCHAR(255),
  `fechaAltaEvento` DATETIME NOT NULL,
  `fechaBajaEvento` DATETIME,
  `fechaModificacionEvento` DATETIME,
  `estadoEvento` INT NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_evento_usuario` (`idUsuario_id`),
  CONSTRAINT `fk_evento_usuario` FOREIGN KEY (`idUsuario_id`) REFERENCES `Usuario` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `ArchivoEvento` (
  `id` CHAR(36) NOT NULL,
  `createdAt` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updatedAt` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deletedAt` DATETIME NULL,
  `fechaAltaArchivoEvento` DATETIME NOT NULL,
  `fechaBajaArchivoEvento` DATETIME NOT NULL,
  `tipoArchivoEvento` VARCHAR(255) NOT NULL,
  `urlArchivoEvento` VARCHAR(500) NOT NULL,
  `idEvento` CHAR(36) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_archivoevento_evento` (`idEvento`),
  CONSTRAINT `fk_archivoevento_evento` FOREIGN KEY (`idEvento`) REFERENCES `Evento` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `Colaboracion` (
  `id` CHAR(36) NOT NULL,
  `createdAt` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updatedAt` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deletedAt` DATETIME NULL,
  `idEvento` CHAR(36) NOT NULL,
  `idUsuario` CHAR(36) NOT NULL,
  `fechaAltaColaboracion` DATETIME NOT NULL,
  `fechaBajaColaboracion` DATETIME NOT NULL,
  `estado` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_colab_evento` (`idEvento`),
  KEY `idx_colab_usuario` (`idUsuario`),
  CONSTRAINT `fk_colab_evento` FOREIGN KEY (`idEvento`) REFERENCES `Evento` (`id`),
  CONSTRAINT `fk_colab_usuario` FOREIGN KEY (`idUsuario`) REFERENCES `Usuario` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `Interes` (
  `id` CHAR(36) NOT NULL,
  `createdAt` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updatedAt` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deletedAt` DATETIME NULL,
  `idEvento_id` CHAR(36) NOT NULL,
  `idUsuario_id` CHAR(36) NOT NULL,
  `fechaAltaInteres` DATETIME NOT NULL,
  `fechaBajaInteres` DATETIME NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_interes_evento` (`idEvento_id`),
  KEY `idx_interes_usuario` (`idUsuario_id`),
  CONSTRAINT `fk_interes_evento` FOREIGN KEY (`idEvento_id`) REFERENCES `Evento` (`id`),
  CONSTRAINT `fk_interes_usuario` FOREIGN KEY (`idUsuario_id`) REFERENCES `Usuario` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Comentarios y denuncias
CREATE TABLE IF NOT EXISTS `Comentario` (
  `id` CHAR(36) NOT NULL,
  `createdAt` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updatedAt` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deletedAt` DATETIME NULL,
  `comentario` VARCHAR(1000) NOT NULL,
  `idUsuarioComentado` CHAR(36) NOT NULL,
  `idUsuarioComentador` CHAR(36) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_comentario_comentado` (`idUsuarioComentado`),
  KEY `idx_comentario_comentador` (`idUsuarioComentador`),
  CONSTRAINT `fk_comentario_comentado` FOREIGN KEY (`idUsuarioComentado`) REFERENCES `Usuario` (`id`),
  CONSTRAINT `fk_comentario_comentador` FOREIGN KEY (`idUsuarioComentador`) REFERENCES `Usuario` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `Denuncia` (
  `id` CHAR(36) NOT NULL,
  `createdAt` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updatedAt` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deletedAt` DATETIME NULL,
  `idComentario` CHAR(36) NOT NULL,
  `idUsuario` CHAR(36) NOT NULL,
  `motivoDenuncia` VARCHAR(1000),
  PRIMARY KEY (`id`),
  KEY `idx_denuncia_comentario` (`idComentario`),
  KEY `idx_denuncia_usuario` (`idUsuario`),
  CONSTRAINT `fk_denuncia_comentario` FOREIGN KEY (`idComentario`) REFERENCES `Comentario` (`id`),
  CONSTRAINT `fk_denuncia_usuario` FOREIGN KEY (`idUsuario`) REFERENCES `Usuario` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Mensajería
CREATE TABLE IF NOT EXISTS `Chat` (
  `id` CHAR(36) NOT NULL,
  `createdAt` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updatedAt` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deletedAt` DATETIME NULL,
  `atributo` VARCHAR(255),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `ChatUsuario` (
  `id` CHAR(36) NOT NULL,
  `createdAt` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updatedAt` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deletedAt` DATETIME NULL,
  `silenciado` BOOLEAN NOT NULL,
  `idUsuario` CHAR(36) NOT NULL,
  `idChat` CHAR(36) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_chatusuario_usuario` (`idUsuario`),
  KEY `idx_chatusuario_chat` (`idChat`),
  CONSTRAINT `fk_chatusuario_usuario` FOREIGN KEY (`idUsuario`) REFERENCES `Usuario` (`id`),
  CONSTRAINT `fk_chatusuario_chat` FOREIGN KEY (`idChat`) REFERENCES `Chat` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `Mensaje` (
  `id` CHAR(36) NOT NULL,
  `createdAt` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updatedAt` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deletedAt` DATETIME NULL,
  `idEmisor` CHAR(36) NOT NULL,
  `idChat` CHAR(36) NOT NULL,
  `mensaje` TEXT,
  PRIMARY KEY (`id`),
  KEY `idx_mensaje_emisor` (`idEmisor`),
  KEY `idx_mensaje_chat` (`idChat`),
  CONSTRAINT `fk_mensaje_emisor` FOREIGN KEY (`idEmisor`) REFERENCES `Usuario` (`id`),
  CONSTRAINT `fk_mensaje_chat` FOREIGN KEY (`idChat`) REFERENCES `Chat` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Notificaciones
CREATE TABLE IF NOT EXISTS `Notificacion` (
  `id` CHAR(36) NOT NULL,
  `createdAt` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updatedAt` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deletedAt` DATETIME NULL,
  `fechaAltaNotificacion` DATETIME NOT NULL,
  `fechaBajaNotificacion` DATETIME NOT NULL,
  `fechaLecturaNotificacion` DATETIME NULL,
  `contenidoNotificacion` VARCHAR(1000) NOT NULL,
  `idTipoNotificacion` CHAR(36) NOT NULL,
  `idUsuario` CHAR(36) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_notif_tipo` (`idTipoNotificacion`),
  KEY `idx_notif_usuario` (`idUsuario`),
  CONSTRAINT `fk_notif_tipo` FOREIGN KEY (`idTipoNotificacion`) REFERENCES `TipoNotificacion` (`id`),
  CONSTRAINT `fk_notif_usuario` FOREIGN KEY (`idUsuario`) REFERENCES `Usuario` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `TipoNotificacionUsuario` (
  `id` CHAR(36) NOT NULL,
  `createdAt` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updatedAt` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deletedAt` DATETIME NULL,
  `fechaAltaTipoNotificacion` DATETIME NOT NULL,
  `fechaBajaTipoNotificacion` DATETIME NOT NULL,
  `notificacionUsuarioActiva` BOOLEAN NOT NULL,
  `idTipoNotificacion` CHAR(36) NOT NULL,
  `idUsuario` CHAR(36) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_tipo_notif_usuario_tipo` (`idTipoNotificacion`),
  KEY `idx_tipo_notif_usuario_usuario` (`idUsuario`),
  CONSTRAINT `fk_tipo_notif_usuario_tipo` FOREIGN KEY (`idTipoNotificacion`) REFERENCES `TipoNotificacion` (`id`),
  CONSTRAINT `fk_tipo_notif_usuario_usuario` FOREIGN KEY (`idUsuario`) REFERENCES `Usuario` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Recomendaciones
CREATE TABLE IF NOT EXISTS `RecomendacionEventoDiaria` (
  `id` CHAR(36) NOT NULL,
  `createdAt` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updatedAt` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deletedAt` DATETIME NULL,
  `cantidadInteresados` INT NOT NULL,
  `evento_id` CHAR(36) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_recomendacion_evento` (`evento_id`),
  CONSTRAINT `fk_recomendacion_evento` FOREIGN KEY (`evento_id`) REFERENCES `Evento` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Reportes
CREATE TABLE IF NOT EXISTS `ReporteAdminMensual` (
  `id` CHAR(36) NOT NULL,
  `createdAt` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updatedAt` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deletedAt` DATETIME NULL,
  `valorMetrica` DECIMAL(19,2) NOT NULL,
  `tipoMetrica_id` CHAR(36) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_reporte_metrica` (`tipoMetrica_id`),
  CONSTRAINT `fk_reporte_metrica` FOREIGN KEY (`tipoMetrica_id`) REFERENCES `TipoMetrica` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `TipoReporteDiarioUsuario` (
  `id` CHAR(36) NOT NULL,
  `createdAt` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updatedAt` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deletedAt` DATETIME NULL,
  `valorMetrica` DECIMAL(19,2) NOT NULL,
  `tipoMetrica_id` CHAR(36) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_tiporeporte_metrica` (`tipoMetrica_id`),
  CONSTRAINT `fk_tiporeporte_metrica` FOREIGN KEY (`tipoMetrica_id`) REFERENCES `TipoMetrica` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Calendario fechas (depends on Evento, Calendario)
CREATE TABLE IF NOT EXISTS `FechaCalendario` (
  `id` CHAR(36) NOT NULL,
  `createdAt` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updatedAt` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deletedAt` DATETIME NULL,
  `fechaHoraFCalendario` DATETIME,
  `idCalendario` CHAR(36),
  `idEvento` CHAR(36),
  PRIMARY KEY (`id`),
  KEY `idx_fechacal_calendario` (`idCalendario`),
  KEY `idx_fechacal_evento` (`idEvento`),
  CONSTRAINT `fk_fechacal_calendario` FOREIGN KEY (`idCalendario`) REFERENCES `Calendario` (`id`),
  CONSTRAINT `fk_fechacal_evento` FOREIGN KEY (`idEvento`) REFERENCES `Evento` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Operaciones de backup
CREATE TABLE IF NOT EXISTS `OperacionBD` (
  `id` CHAR(36) NOT NULL,
  `createdAt` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updatedAt` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deletedAt` DATETIME NULL,
  `nombreOperacion` VARCHAR(255) NOT NULL,
  `tipo` VARCHAR(50) NOT NULL,
  `usuario_id` CHAR(36) NOT NULL,
  `estado` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_operacion_usuario` (`usuario_id`),
  CONSTRAINT `fk_operacion_usuario` FOREIGN KEY (`usuario_id`) REFERENCES `Usuario` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

SET FOREIGN_KEY_CHECKS = 1;
