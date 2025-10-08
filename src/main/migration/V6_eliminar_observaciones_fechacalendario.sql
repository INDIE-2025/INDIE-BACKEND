-- V6: Eliminar columna observaciones de FechaCalendario
-- Eliminamos la columna observaciones que ya no se usa en el sistema

ALTER TABLE FechaCalendario DROP COLUMN observaciones;