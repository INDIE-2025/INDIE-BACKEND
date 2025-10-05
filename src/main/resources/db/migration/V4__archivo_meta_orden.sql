-- Add metadata and order columns to Archivo
ALTER TABLE `Archivo`
  ADD COLUMN `titulo` VARCHAR(255) NULL AFTER `contentType`,
  ADD COLUMN `descripcion` VARCHAR(1000) NULL AFTER `titulo`,
  ADD COLUMN `orden` INT NULL DEFAULT 0 AFTER `descripcion`;

