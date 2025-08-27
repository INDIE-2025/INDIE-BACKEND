ALTER TABLE usuario
    ADD activo datetime NULL;

CREATE TABLE verification_token
(
    id          BIGINT AUTO_INCREMENT NOT NULL,
    token       VARCHAR(255)          NULL,
    usuario_id  VARCHAR(255)          NULL,
    expiry_date datetime              NULL,
    CONSTRAINT pk_verificationtoken PRIMARY KEY (id)
);

ALTER TABLE usuario
    ADD fecha_verificacion datetime NULL;

ALTER TABLE verification_token
    ADD CONSTRAINT uc_verificationtoken_usuario UNIQUE (usuario_id);

ALTER TABLE verification_token
    ADD CONSTRAINT FK_VERIFICATIONTOKEN_ON_USUARIO FOREIGN KEY (usuario_id) REFERENCES usuario (id);

ALTER TABLE usuario
    DROP COLUMN activo;

ALTER TABLE colaboracion
    DROP COLUMN estado;

ALTER TABLE colaboracion
    ADD estado VARCHAR(255) NOT NULL;

ALTER TABLE operacionbd
    DROP COLUMN estado;

ALTER TABLE operacionbd
    DROP COLUMN tipo;

ALTER TABLE operacionbd
    ADD estado VARCHAR(255) NOT NULL;

ALTER TABLE operacionbd
    ADD tipo VARCHAR(255) NOT NULL;

ALTER TABLE reporte_admin_mensual
    MODIFY valor_metrica DECIMAL;

ALTER TABLE tipo_reporte_diario_usuario
    MODIFY valor_metrica DECIMAL;