-- Generado por Oracle SQL Developer Data Modeler 23.1.0.087.0806
--   en:        2023-12-17 14:49:02 CET
--   sitio:      Oracle Database 11g
--   tipo:      Oracle Database 11g



-- predefined type, no DDL - MDSYS.SDO_GEOMETRY

-- predefined type, no DDL - XMLTYPE

CREATE TABLE clase (
    nombre              VARCHAR2(10 CHAR) NOT NULL,
    horario             DATE,
    num_personas_maximo INTEGER,
    objetivo            VARCHAR2(10 CHAR),
    virtual             CHAR(1)
);

ALTER TABLE clase ADD CONSTRAINT clase_pk PRIMARY KEY ( nombre );

CREATE TABLE dieta (
    entrenador_codigo       VARCHAR2(20 CHAR) NOT NULL,
    usuario_codigo          VARCHAR2(20 CHAR),
    id                      VARCHAR2(20 CHAR) NOT NULL,
    objetivo                VARCHAR2(20 CHAR),
    informacion_nutricional VARCHAR2(20 CHAR),
    tabla_comidas           VARCHAR2(20 CHAR)
);

COMMENT ON COLUMN dieta.informacion_nutricional IS
    'Que tipo de personas pueden o no consumir la dieta; (veganos, vegetarianos, celiacos,...)';

CREATE UNIQUE INDEX dieta__idx ON
    dieta (
        usuario_codigo
    ASC );

ALTER TABLE dieta ADD CONSTRAINT dieta_pk PRIMARY KEY ( id );

CREATE TABLE ejercicio (
    nombre      VARCHAR2(10 CHAR) NOT NULL,
    video       VARCHAR2(10 CHAR),
    popularidad VARCHAR2(10 CHAR),
    dificultad  VARCHAR2(10 CHAR)
);

ALTER TABLE ejercicio ADD CONSTRAINT ejercicio_pk PRIMARY KEY ( nombre );

CREATE TABLE entrenador (
    codigo          VARCHAR2(10 CHAR) NOT NULL,
    horario         DATE,
    especialidad    VARCHAR2(10 CHAR),
    gimnasio_codigo VARCHAR2(20 CHAR),
    telefono        INTEGER
);

ALTER TABLE entrenador ADD CONSTRAINT entrenador_pk PRIMARY KEY ( codigo );

CREATE TABLE funcionalidades (
    estadisticas_mensuales CHAR(1),
    dietas_personalizadas  CHAR(1),
    clases_online          CHAR(1),
    aplicacion_movil       CHAR(1),
    personalizacion_marca  CHAR(1),
    calendario             CHAR(1),
    fun_id                 NUMBER NOT NULL
);

ALTER TABLE funcionalidades ADD CONSTRAINT funcionalidades_pk PRIMARY KEY ( fun_id );

CREATE TABLE gimnasio (
    codigo         VARCHAR2(20 CHAR) NOT NULL,
    ubicacion      VARCHAR2(10 CHAR) NOT NULL,
    nombre         VARCHAR2(10 CHAR),
    patrocinadores VARCHAR2(10 CHAR),
    idioma         VARCHAR2(10 CHAR),
    pagina_web     VARCHAR2(10 CHAR),
    telefono       INTEGER
);

ALTER TABLE gimnasio ADD CONSTRAINT gimnasio_pk PRIMARY KEY ( codigo );

ALTER TABLE gimnasio ADD CONSTRAINT gimnasio_ubicacion_un UNIQUE ( ubicacion );

CREATE TABLE grupo_muscular (
    material_codigo  VARCHAR2(10 CHAR) NOT NULL,
    ejercicio_nombre VARCHAR2(10 CHAR) NOT NULL,
    grupomuscular    VARCHAR2(20 CHAR) NOT NULL
);

ALTER TABLE grupo_muscular ADD CONSTRAINT grupo_muscular_pk PRIMARY KEY ( material_codigo,
                                                                          ejercicio_nombre );

CREATE TABLE material (
    codigo           VARCHAR2(10 CHAR) NOT NULL,
    nombre           VARCHAR2(10 CHAR),
    explicacion      VARCHAR2(10 CHAR),
    ejercicio_nombre VARCHAR2(10 CHAR) NOT NULL,
    gimnasio_codigo  VARCHAR2(20 CHAR)
);

CREATE UNIQUE INDEX material__idx ON
    material (
        ejercicio_nombre
    ASC );

ALTER TABLE material ADD CONSTRAINT material_pk PRIMARY KEY ( codigo );

CREATE TABLE relation_1 (
    gimnasio_codigo VARCHAR2(20 CHAR) NOT NULL,
    usuario_codigo  VARCHAR2(10 CHAR) NOT NULL
);

ALTER TABLE relation_1 ADD CONSTRAINT relation_1_pk PRIMARY KEY ( gimnasio_codigo,
                                                                  usuario_codigo );

CREATE TABLE relation_3 (
    entrenador_codigo VARCHAR2(10 CHAR) NOT NULL,
    clase_nombre      VARCHAR2(10 CHAR) NOT NULL
);

ALTER TABLE relation_3 ADD CONSTRAINT relation_3_pk PRIMARY KEY ( entrenador_codigo,
                                                                  clase_nombre );

CREATE TABLE relation_32 (
    gimnasio_codigo  VARCHAR2(20 CHAR) NOT NULL,
    tarifa_tarifa_id NUMBER NOT NULL
);

ALTER TABLE relation_32 ADD CONSTRAINT relation_32_pk PRIMARY KEY ( gimnasio_codigo,
                                                                    tarifa_tarifa_id );

CREATE TABLE relation_34 (
    tarifa_tarifa_id   NUMBER NOT NULL,
    funcionalidades_id NUMBER NOT NULL
);

ALTER TABLE relation_34 ADD CONSTRAINT relation_34_pk PRIMARY KEY ( tarifa_tarifa_id,
                                                                    funcionalidades_id );

CREATE TABLE rutina (
    num_repeticiones           INTEGER NOT NULL,
    num_series                 INTEGER NOT NULL,
    tiempo_descanso_series     INTEGER,
    tiempo_descanso_ejercicios NUMBER,
    ejercicio_nombre           VARCHAR2(10 CHAR) NOT NULL,
    dificultad                 VARCHAR2(10 CHAR),
    objetivo                   VARCHAR2(20 CHAR),
    entrenador_codigo          VARCHAR2(10 CHAR) NOT NULL,
    usuario_codigo             VARCHAR2(10 CHAR) NOT NULL
);

ALTER TABLE rutina
    ADD CONSTRAINT rutina_pk PRIMARY KEY ( ejercicio_nombre,
                                           entrenador_codigo,
                                           usuario_codigo );

CREATE TABLE sesion (
    usuario_codigo    VARCHAR2(10 CHAR) NOT NULL,
    clase_nombre      VARCHAR2(10 CHAR) NOT NULL,
    marca             VARCHAR2(20 CHAR),
    pulsaciones       INTEGER,
    cansancio         INTEGER,
    autoevaluacion    VARCHAR2(20 CHAR),
    objetivos_mejorar VARCHAR2(20 CHAR)
);

COMMENT ON COLUMN sesion.marca IS
    'Tiempo empleado, repeticiones realizadas...';

ALTER TABLE sesion ADD CONSTRAINT sesion_pk PRIMARY KEY ( usuario_codigo,
                                                          clase_nombre );

CREATE TABLE tarifa (
    nombre    VARCHAR2(20 CHAR),
    precio    NUMBER,
    tarifa_id NUMBER NOT NULL
);

ALTER TABLE tarifa ADD CONSTRAINT tarifa_pk PRIMARY KEY ( tarifa_id );

CREATE TABLE usuario (
    codigo           VARCHAR2(10 CHAR) NOT NULL,
    dni              VARCHAR2(10 CHAR) NOT NULL,
    nombre           VARCHAR2(20 CHAR),
    apellidos        VARCHAR2(10 CHAR),
    correo           VARCHAR2(10 CHAR),
    fecha_nacimiento DATE,
    nivel            VARCHAR2(10 CHAR),
    sexo             CHAR(1 CHAR),
    dieta_id         VARCHAR2(20 CHAR)
);

CREATE UNIQUE INDEX usuario__idx ON
    usuario (
        dieta_id
    ASC );

ALTER TABLE usuario ADD CONSTRAINT usuario_pk PRIMARY KEY ( codigo );

ALTER TABLE usuario ADD CONSTRAINT usuario_dni_un UNIQUE ( dni );

ALTER TABLE dieta
    ADD CONSTRAINT dieta_entrenador_fk FOREIGN KEY ( entrenador_codigo )
        REFERENCES entrenador ( codigo );

ALTER TABLE dieta
    ADD CONSTRAINT dieta_usuario_fk FOREIGN KEY ( usuario_codigo )
        REFERENCES usuario ( codigo );

ALTER TABLE entrenador
    ADD CONSTRAINT entrenador_gimnasio_fk FOREIGN KEY ( gimnasio_codigo )
        REFERENCES gimnasio ( codigo );

ALTER TABLE entrenador
    ADD CONSTRAINT entrenador_usuario_fk FOREIGN KEY ( codigo )
        REFERENCES usuario ( codigo );

ALTER TABLE grupo_muscular
    ADD CONSTRAINT grupo_muscular_ejercicio_fk FOREIGN KEY ( ejercicio_nombre )
        REFERENCES ejercicio ( nombre );

ALTER TABLE grupo_muscular
    ADD CONSTRAINT grupo_muscular_material_fk FOREIGN KEY ( material_codigo )
        REFERENCES material ( codigo );

ALTER TABLE material
    ADD CONSTRAINT material_ejercicio_fk FOREIGN KEY ( ejercicio_nombre )
        REFERENCES ejercicio ( nombre );

ALTER TABLE material
    ADD CONSTRAINT material_gimnasio_fk FOREIGN KEY ( gimnasio_codigo )
        REFERENCES gimnasio ( codigo );

ALTER TABLE relation_1
    ADD CONSTRAINT relation_1_gimnasio_fk FOREIGN KEY ( gimnasio_codigo )
        REFERENCES gimnasio ( codigo );

ALTER TABLE relation_1
    ADD CONSTRAINT relation_1_usuario_fk FOREIGN KEY ( usuario_codigo )
        REFERENCES usuario ( codigo );

ALTER TABLE relation_3
    ADD CONSTRAINT relation_3_clase_fk FOREIGN KEY ( clase_nombre )
        REFERENCES clase ( nombre );

ALTER TABLE relation_3
    ADD CONSTRAINT relation_3_entrenador_fk FOREIGN KEY ( entrenador_codigo )
        REFERENCES entrenador ( codigo );

ALTER TABLE relation_32
    ADD CONSTRAINT relation_32_gimnasio_fk FOREIGN KEY ( gimnasio_codigo )
        REFERENCES gimnasio ( codigo );

ALTER TABLE relation_32
    ADD CONSTRAINT relation_32_tarifa_fk FOREIGN KEY ( tarifa_tarifa_id )
        REFERENCES tarifa ( tarifa_id );

ALTER TABLE relation_34
    ADD CONSTRAINT relation_34_funcionalidades_fk FOREIGN KEY ( funcionalidades_id )
        REFERENCES funcionalidades ( fun_id );

ALTER TABLE relation_34
    ADD CONSTRAINT relation_34_tarifa_fk FOREIGN KEY ( tarifa_tarifa_id )
        REFERENCES tarifa ( tarifa_id );

ALTER TABLE rutina
    ADD CONSTRAINT rutina_ejercicio_fk FOREIGN KEY ( ejercicio_nombre )
        REFERENCES ejercicio ( nombre );

ALTER TABLE rutina
    ADD CONSTRAINT rutina_entrenador_fk FOREIGN KEY ( entrenador_codigo )
        REFERENCES entrenador ( codigo );

ALTER TABLE rutina
    ADD CONSTRAINT rutina_usuario_fk FOREIGN KEY ( usuario_codigo )
        REFERENCES usuario ( codigo );

ALTER TABLE sesion
    ADD CONSTRAINT sesion_clase_fk FOREIGN KEY ( clase_nombre )
        REFERENCES clase ( nombre );

ALTER TABLE sesion
    ADD CONSTRAINT sesion_usuario_fk FOREIGN KEY ( usuario_codigo )
        REFERENCES usuario ( codigo );

ALTER TABLE usuario
    ADD CONSTRAINT usuario_dieta_fk FOREIGN KEY ( dieta_id )
        REFERENCES dieta ( id );

CREATE SEQUENCE funcionalidades_fun_id_seq START WITH 1 NOCACHE ORDER;

CREATE OR REPLACE TRIGGER funcionalidades_fun_id_trg BEFORE
    INSERT ON funcionalidades
    FOR EACH ROW
    WHEN ( new.fun_id IS NULL )
BEGIN
    :new.fun_id := funcionalidades_fun_id_seq.nextval;
END;
/

CREATE SEQUENCE tarifa_tarifa_id_seq START WITH 1 NOCACHE ORDER;

CREATE OR REPLACE TRIGGER tarifa_tarifa_id_trg BEFORE
    INSERT ON tarifa
    FOR EACH ROW
    WHEN ( new.tarifa_id IS NULL )
BEGIN
    :new.tarifa_id := tarifa_tarifa_id_seq.nextval;
END;
/



-- Informe de Resumen de Oracle SQL Developer Data Modeler: 
-- 
-- CREATE TABLE                            16
-- CREATE INDEX                             3
-- ALTER TABLE                             40
-- CREATE VIEW                              0
-- ALTER VIEW                               0
-- CREATE PACKAGE                           0
-- CREATE PACKAGE BODY                      0
-- CREATE PROCEDURE                         0
-- CREATE FUNCTION                          0
-- CREATE TRIGGER                           2
-- ALTER TRIGGER                            0
-- CREATE COLLECTION TYPE                   0
-- CREATE STRUCTURED TYPE                   0
-- CREATE STRUCTURED TYPE BODY              0
-- CREATE CLUSTER                           0
-- CREATE CONTEXT                           0
-- CREATE DATABASE                          0
-- CREATE DIMENSION                         0
-- CREATE DIRECTORY                         0
-- CREATE DISK GROUP                        0
-- CREATE ROLE                              0
-- CREATE ROLLBACK SEGMENT                  0
-- CREATE SEQUENCE                          2
-- CREATE MATERIALIZED VIEW                 0
-- CREATE MATERIALIZED VIEW LOG             0
-- CREATE SYNONYM                           0
-- CREATE TABLESPACE                        0
-- CREATE USER                              0
-- 
-- DROP TABLESPACE                          0
-- DROP DATABASE                            0
-- 
-- REDACTION POLICY                         0
-- 
-- ORDS DROP SCHEMA                         0
-- ORDS ENABLE SCHEMA                       0
-- ORDS ENABLE OBJECT                       0
-- 
-- ERRORS                                   0
-- WARNINGS                                 0
