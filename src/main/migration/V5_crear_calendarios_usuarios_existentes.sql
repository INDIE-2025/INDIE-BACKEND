-- V5_crear_calendarios_usuarios_existentes.sql
-- Migración para crear calendarios para usuarios existentes que no tienen uno

-- Esta migración crea calendarios para usuarios que fueron registrados antes
-- de implementar la creación automática de calendarios

DO $$
DECLARE
    usuario_record RECORD;
    new_calendario_id UUID;
BEGIN
    -- Iterar sobre todos los usuarios que no tienen calendario
    FOR usuario_record IN 
        SELECT u.id, u.username, u.nombre_usuario 
        FROM usuario u 
        WHERE u.calendario_id IS NULL 
          AND u.deleted_at IS NULL
    LOOP
        -- Generar un nuevo ID para el calendario
        new_calendario_id := gen_random_uuid();
        
        -- Crear el calendario
        INSERT INTO calendario (id, zona_horaria, created_at, updated_at) 
        VALUES (
            new_calendario_id, 
            'America/Argentina/Buenos_Aires', 
            NOW(), 
            NOW()
        );
        
        -- Asociar el calendario al usuario
        UPDATE usuario 
        SET calendario_id = new_calendario_id, updated_at = NOW()
        WHERE id = usuario_record.id;
        
        -- Log del progreso
        RAISE NOTICE 'Calendario creado para usuario: % (ID: %)', 
                     usuario_record.username, usuario_record.id;
    END LOOP;
    
    -- Mostrar resumen
    RAISE NOTICE 'Migración completada. Calendarios creados para usuarios existentes.';
END $$;