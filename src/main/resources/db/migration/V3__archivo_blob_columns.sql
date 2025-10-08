-- Add optional blob columns to Archivo for binary content storage
ALTER TABLE `Archivo`
  ADD COLUMN `contenido` LONGBLOB NULL AFTER `idObjeto`,
  ADD COLUMN `contentType` VARCHAR(100) NULL AFTER `contenido`;

