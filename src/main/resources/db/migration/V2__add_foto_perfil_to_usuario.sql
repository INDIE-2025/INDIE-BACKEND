-- Add profile photo columns to Usuario
ALTER TABLE `Usuario`
  ADD COLUMN `fotoPerfil` LONGBLOB NULL AFTER `subTipoUsuario_id`,
  ADD COLUMN `fotoPerfilContentType` VARCHAR(100) NULL AFTER `fotoPerfil`;

