-- Generado por Oracle SQL Developer Data Modeler 23.1.0.087.0806
--   en:        2023-10-06 17:18:43 CEST
--   sitio:      Oracle Database 11g
--   tipo:      Oracle Database 11g



-- predefined type, no DDL - MDSYS.SDO_GEOMETRY

-- predefined type, no DDL - XMLTYPE

CREATE TABLE debate (
    codigo                 INTEGER NOT NULL,
    fecha_cierre           DATE,
    mensaje_fecha_creacion DATE NOT NULL,
    mensaje_usuario_codigo INTEGER NOT NULL,
    mensaje_debate_codigo  INTEGER NOT NULL,
    debate_codigo          INTEGER,
    tema_nombre            VARCHAR2(20 CHAR)
);

CREATE UNIQUE INDEX debate__idx ON
    debate (
        mensaje_fecha_creacion
    ASC,
        mensaje_usuario_codigo
    ASC,
        mensaje_debate_codigo
    ASC );

CREATE UNIQUE INDEX debate__idxv1 ON
    debate (
        debate_codigo
    ASC );

ALTER TABLE debate ADD CONSTRAINT debate_pk PRIMARY KEY ( codigo );

CREATE TABLE mensaje (
    fecha_creacion DATE NOT NULL,
    contenido      VARCHAR2(200 CHAR),
    titulo         VARCHAR2(30 CHAR),
    usuario_codigo INTEGER NOT NULL,
    debate_codigo  INTEGER NOT NULL
);

ALTER TABLE mensaje
    ADD CONSTRAINT mensaje_pk PRIMARY KEY ( fecha_creacion,
                                            usuario_codigo,
                                            debate_codigo );

CREATE TABLE relation_11 (
    usuario_codigo INTEGER NOT NULL,
    tema_nombre    VARCHAR2(20 CHAR) NOT NULL
);

ALTER TABLE relation_11 ADD CONSTRAINT relation_11_pk PRIMARY KEY ( usuario_codigo,
                                                                    tema_nombre );

CREATE TABLE relation_13 (
    usuario_codigo INTEGER NOT NULL,
    debate_codigo  INTEGER NOT NULL
);

ALTER TABLE relation_13 ADD CONSTRAINT relation_13_pk PRIMARY KEY ( usuario_codigo,
                                                                    debate_codigo );

CREATE TABLE tema (
    nombre           VARCHAR2(20 CHAR) NOT NULL,
    descripcion      VARCHAR2(50 CHAR) NOT NULL,
    fecha_asignatura DATE NOT NULL
);

ALTER TABLE tema ADD CONSTRAINT tema_pk PRIMARY KEY ( nombre );

CREATE TABLE usr_tema (
    usuario_codigo INTEGER NOT NULL,
    tema_nombre    VARCHAR2(20 CHAR) NOT NULL
);

ALTER TABLE usr_tema ADD CONSTRAINT usr_tema_pk PRIMARY KEY ( usuario_codigo,
                                                              tema_nombre );

CREATE TABLE usuario (
    codigo       INTEGER NOT NULL,
    email        VARCHAR2(40 CHAR),
    alias        VARCHAR2(40 CHAR),
    password     VARCHAR2(40 CHAR) NOT NULL,
    ciudad       VARCHAR2(20 CHAR),
    firma        VARCHAR2(40 CHAR),
    avatar       VARCHAR2(40 CHAR),
    activo       CHAR(1),
    estado       CHAR(1),
    fecha_alta   DATE,
    web_personal VARCHAR2(40),
    ocupacion    VARCHAR2(40)
);

ALTER TABLE usuario ADD CONSTRAINT usuario_pk PRIMARY KEY ( codigo );

ALTER TABLE usuario ADD CONSTRAINT usuario_email_un UNIQUE ( email );

ALTER TABLE usuario ADD CONSTRAINT usuario_alias_un UNIQUE ( alias );

ALTER TABLE debate
    ADD CONSTRAINT debate_debate_fk FOREIGN KEY ( debate_codigo )
        REFERENCES debate ( codigo );

ALTER TABLE debate
    ADD CONSTRAINT debate_mensaje_fk FOREIGN KEY ( mensaje_fecha_creacion,
                                                   mensaje_usuario_codigo,
                                                   mensaje_debate_codigo )
        REFERENCES mensaje ( fecha_creacion,
                             usuario_codigo,
                             debate_codigo );

ALTER TABLE debate
    ADD CONSTRAINT debate_tema_fk FOREIGN KEY ( tema_nombre )
        REFERENCES tema ( nombre );

ALTER TABLE mensaje
    ADD CONSTRAINT mensaje_debate_fk FOREIGN KEY ( debate_codigo )
        REFERENCES debate ( codigo );

ALTER TABLE mensaje
    ADD CONSTRAINT mensaje_usuario_fk FOREIGN KEY ( usuario_codigo )
        REFERENCES usuario ( codigo );

ALTER TABLE relation_11
    ADD CONSTRAINT relation_11_tema_fk FOREIGN KEY ( tema_nombre )
        REFERENCES tema ( nombre );

ALTER TABLE relation_11
    ADD CONSTRAINT relation_11_usuario_fk FOREIGN KEY ( usuario_codigo )
        REFERENCES usuario ( codigo );

ALTER TABLE relation_13
    ADD CONSTRAINT relation_13_debate_fk FOREIGN KEY ( debate_codigo )
        REFERENCES debate ( codigo );

ALTER TABLE relation_13
    ADD CONSTRAINT relation_13_usuario_fk FOREIGN KEY ( usuario_codigo )
        REFERENCES usuario ( codigo );

ALTER TABLE usr_tema
    ADD CONSTRAINT usr_tema_tema_fk FOREIGN KEY ( tema_nombre )
        REFERENCES tema ( nombre );

ALTER TABLE usr_tema
    ADD CONSTRAINT usr_tema_usuario_fk FOREIGN KEY ( usuario_codigo )
        REFERENCES usuario ( codigo );



-- Informe de Resumen de Oracle SQL Developer Data Modeler: 
-- 
-- CREATE TABLE                             7
-- CREATE INDEX                             2
-- ALTER TABLE                             20
-- CREATE VIEW                              0
-- ALTER VIEW                               0
-- CREATE PACKAGE                           0
-- CREATE PACKAGE BODY                      0
-- CREATE PROCEDURE                         0
-- CREATE FUNCTION                          0
-- CREATE TRIGGER                           0
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
-- CREATE SEQUENCE                          0
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
