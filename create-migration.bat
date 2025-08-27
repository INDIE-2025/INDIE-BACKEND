@echo off
if "%~2"=="" (
    echo Uso: create-migration.bat ^<numero_version^> ^<descripcion^>
    echo Ejemplo: create-migration.bat 5 add_social_media_columns
    exit /b 1
)

set VERSION=%1
set DESCRIPTION=%2
set FILENAME=V%VERSION%__%DESCRIPTION%.sql
set MIGRATION_PATH=src\main\resources\db\migration\%FILENAME%

if not exist "src\main\resources\db\migration" mkdir "src\main\resources\db\migration"

(
echo -- Migración: %DESCRIPTION%
echo -- Versión: V%VERSION%
echo -- Fecha: %date% %time%
echo.
echo -- Agregar tu SQL aquí
echo.
) > "%MIGRATION_PATH%"

echo Migracion creada: %MIGRATION_PATH%
echo Edita el archivo y luego ejecuta: .\gradlew flywayMigrate