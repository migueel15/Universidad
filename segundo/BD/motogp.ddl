-- Generado por Oracle SQL Developer Data Modeler 23.1.0.087.0806
--   en:        2023-10-04 14:33:07 CEST
--   sitio:      Oracle Database 12cR2
--   tipo:      Oracle Database 12cR2



CREATE TABLESPACE ts_alumnos 
--  WARNING: Tablespace has no data files defined 
 LOGGING ONLINE
    EXTENT MANAGEMENT LOCAL AUTOALLOCATE
FLASHBACK ON;

CREATE USER ubd4432 IDENTIFIED BY ACCOUNT UNLOCK ;

-- predefined type, no DDL - MDSYS.SDO_GEOMETRY

-- predefined type, no DDL - XMLTYPE

CREATE TABLE ubd4432.acto_piloto (
    acto_publico_fecha DATE NOT NULL,
    piloto_nombre      VARCHAR2(20 CHAR) NOT NULL
)
PCTFREE 10 PCTUSED 40 TABLESPACE ts_alumnos LOGGING
    STORAGE ( PCTINCREASE 0 MINEXTENTS 1 MAXEXTENTS UNLIMITED FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT )
NO INMEMORY;

CREATE UNIQUE INDEX ubd4432.acto_piloto_pk ON
    ubd4432.acto_piloto (
        acto_publico_fecha
    ASC,
        piloto_nombre
    ASC )
        TABLESPACE ts_alumnos PCTFREE 10
            STORAGE ( PCTINCREASE 0 MINEXTENTS 1 MAXEXTENTS UNLIMITED FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT )
        LOGGING;

ALTER TABLE ubd4432.acto_piloto
    ADD CONSTRAINT acto_piloto_pk PRIMARY KEY ( acto_publico_fecha,
                                                piloto_nombre )
        USING INDEX ubd4432.acto_piloto_pk;

CREATE TABLE ubd4432.acto_publico (
    fecha            DATE NOT NULL,
    direccion        VARCHAR2(20 CHAR),
    descripcion      VARCHAR2(50 CHAR),
    persona_contacto VARCHAR2(10 CHAR),
    numero_contacto  NUMBER(*, 0)
)
PCTFREE 10 PCTUSED 40 TABLESPACE ts_alumnos LOGGING
    STORAGE ( PCTINCREASE 0 MINEXTENTS 1 MAXEXTENTS UNLIMITED FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT )
NO INMEMORY;

CREATE UNIQUE INDEX ubd4432.acto_publico_pk ON
    ubd4432.acto_publico (
        fecha
    ASC )
        TABLESPACE ts_alumnos PCTFREE 10
            STORAGE ( PCTINCREASE 0 MINEXTENTS 1 MAXEXTENTS UNLIMITED FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT )
        LOGGING;

ALTER TABLE ubd4432.acto_publico
    ADD CONSTRAINT acto_publico_pk PRIMARY KEY ( fecha )
        USING INDEX ubd4432.acto_publico_pk;

CREATE TABLE carrera (
    circuito_nombre          VARCHAR2(20 CHAR) NOT NULL,
    piloto_nombre            VARCHAR2(20 CHAR) NOT NULL,
    tipo_neumatico_delantero VARCHAR2(20),
    tipo_neumatico_trasero   VARCHAR2(20),
    caida                    NUMBER,
    tiempo                   NUMBER,
    puntuación               NUMBER
)
LOGGING;

ALTER TABLE carrera ADD CONSTRAINT carrera_pkv1 PRIMARY KEY ( circuito_nombre,
                                                              piloto_nombre );

CREATE TABLE circuito (
    nombre                    VARCHAR2(20 CHAR) NOT NULL,
    fecha                     DATE,
    pais                      VARCHAR2(20 CHAR),
    ciudad                    VARCHAR2(20 CHAR),
    año_inauguración          DATE,
    anchura                   NUMBER,
    posicion_parrilla         INTEGER,
    longitud_total            NUMBER,
    distancia_recta_mas_larga NUMBER,
    numero_curvas_izq         INTEGER,
    numero_curvas_der         INTEGER,
    mapa                      VARCHAR2(20 CHAR),
    numero_vueltas            INTEGER,
    tipo_carrera              VARCHAR2(20 CHAR)
)
LOGGING;

ALTER TABLE circuito ADD CONSTRAINT carrera_pk PRIMARY KEY ( nombre );

ALTER TABLE circuito ADD CONSTRAINT carrera_fecha_un UNIQUE ( fecha );

CREATE TABLE empleado (
    codigo           INTEGER NOT NULL,
    pasaporte        VARCHAR2(20 CHAR),
    nacionalidad     VARCHAR2(20 CHAR),
    nombre           VARCHAR2(20 CHAR),
    fecha_nacimiento DATE,
    equipo_nombre    VARCHAR2(20 CHAR),
    equipo_nombre1   VARCHAR2(20 CHAR) NOT NULL
)
LOGGING;

CREATE UNIQUE INDEX empleado__idx ON
    empleado (
        equipo_nombre
    ASC )
        LOGGING;

ALTER TABLE empleado ADD CONSTRAINT empleado_pk PRIMARY KEY ( codigo,
                                                              equipo_nombre1 );

ALTER TABLE empleado ADD CONSTRAINT empleado_pasaporte_un UNIQUE ( pasaporte );

ALTER TABLE empleado ADD CONSTRAINT empleado_nacionalidad_un UNIQUE ( nacionalidad );

CREATE TABLE equipo (
    nombre                  VARCHAR2(20 CHAR) NOT NULL,
    modelo_moto             VARCHAR2(20 CHAR) NOT NULL,
    direccion_postal        VARCHAR2(20 CHAR) NOT NULL,
    direccion_web           VARCHAR2(20 CHAR) NOT NULL,
    objetivos               VARCHAR2(20 CHAR) NOT NULL,
    foto_oficial            VARCHAR2(50 CHAR) NOT NULL,
    logo                    VARCHAR2(40 CHAR) NOT NULL,
    empleado_codigo         INTEGER,
    empleado_equipo_nombre2 VARCHAR2(20 CHAR)
)
LOGGING;

CREATE UNIQUE INDEX equipo__idx ON
    equipo (
        empleado_codigo
    ASC,
        empleado_equipo_nombre2
    ASC )
        LOGGING;

ALTER TABLE equipo ADD CONSTRAINT equipo_pk PRIMARY KEY ( nombre );

CREATE TABLE oficial (
    nombre            VARCHAR2(20 CHAR) NOT NULL,
    presupuesto       NUMBER,
    año_creación      VARCHAR2(10 CHAR),
    direccion_fabrica VARCHAR2(30 CHAR)
)
LOGGING;

ALTER TABLE oficial ADD CONSTRAINT oficial_pk PRIMARY KEY ( nombre );

CREATE TABLE ubd4432.piloto (
    nombre        VARCHAR2(20 CHAR) NOT NULL,
    dorsal        NUMBER(*, 0),
    pais          VARCHAR2(20 CHAR),
    ciudad        VARCHAR2(20 CHAR),
    peso          NUMBER,
    altura        NUMBER,
    video         VARCHAR2(50 CHAR),
    equipo_nombre VARCHAR2(20 CHAR) NOT NULL
)
PCTFREE 10 PCTUSED 40 TABLESPACE ts_alumnos LOGGING
    STORAGE ( PCTINCREASE 0 MINEXTENTS 1 MAXEXTENTS UNLIMITED FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT )
NO INMEMORY;

CREATE UNIQUE INDEX ubd4432.piloto_dorsal_un ON
    ubd4432.piloto (
        dorsal
    ASC )
        TABLESPACE ts_alumnos PCTFREE 10
            STORAGE ( PCTINCREASE 0 MINEXTENTS 1 MAXEXTENTS UNLIMITED FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT )
        LOGGING;

CREATE UNIQUE INDEX ubd4432.piloto_pk ON
    ubd4432.piloto (
        nombre
    ASC )
        TABLESPACE ts_alumnos PCTFREE 10
            STORAGE ( PCTINCREASE 0 MINEXTENTS 1 MAXEXTENTS UNLIMITED FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT )
        LOGGING;

ALTER TABLE ubd4432.piloto
    ADD CONSTRAINT piloto_pk PRIMARY KEY ( nombre )
        USING INDEX ubd4432.piloto_pk;

ALTER TABLE ubd4432.piloto
    ADD CONSTRAINT piloto_dorsal_un UNIQUE ( dorsal )
        USING INDEX ubd4432.piloto_dorsal_un;

CREATE TABLE piloto_piloto (
    piloto_nombre  VARCHAR2(20 CHAR) NOT NULL,
    piloto_nombre1 VARCHAR2(20 CHAR) NOT NULL
)
LOGGING;

ALTER TABLE piloto_piloto ADD CONSTRAINT relation_8_pk PRIMARY KEY ( piloto_nombre,
                                                                     piloto_nombre1 );

CREATE TABLE tiempos (
    tramo_codigo  INTEGER NOT NULL,
    piloto_nombre VARCHAR2(20 CHAR) NOT NULL,
    vuelta        INTEGER NOT NULL,
    tiempo        NUMBER
)
LOGGING;

ALTER TABLE tiempos ADD CONSTRAINT relation_6_pk PRIMARY KEY ( tramo_codigo,
                                                               piloto_nombre );

CREATE TABLE tramo (
    codigo            INTEGER NOT NULL,
    diferencia_altura INTEGER,
    tipo_asfalto      VARCHAR2(20 CHAR),
    velocidad_meida   NUMBER,
    circuito_nombre   VARCHAR2(20 CHAR) NOT NULL
)
LOGGING;

ALTER TABLE tramo ADD CONSTRAINT tramo_pk PRIMARY KEY ( codigo );

ALTER TABLE ubd4432.acto_piloto
    ADD CONSTRAINT acto_piloto_acto_publico_fk FOREIGN KEY ( acto_publico_fecha )
        REFERENCES ubd4432.acto_publico ( fecha )
    NOT DEFERRABLE;

ALTER TABLE ubd4432.acto_piloto
    ADD CONSTRAINT acto_piloto_piloto_fk FOREIGN KEY ( piloto_nombre )
        REFERENCES ubd4432.piloto ( nombre )
    NOT DEFERRABLE;

ALTER TABLE carrera
    ADD CONSTRAINT carrera_circuito_fk FOREIGN KEY ( circuito_nombre )
        REFERENCES circuito ( nombre )
    NOT DEFERRABLE;

ALTER TABLE carrera
    ADD CONSTRAINT carrera_piloto_fk FOREIGN KEY ( piloto_nombre )
        REFERENCES ubd4432.piloto ( nombre )
    NOT DEFERRABLE;

ALTER TABLE empleado
    ADD CONSTRAINT empleado_equipo_fk FOREIGN KEY ( equipo_nombre )
        REFERENCES equipo ( nombre )
    NOT DEFERRABLE;

ALTER TABLE empleado
    ADD CONSTRAINT empleado_equipo_fkv1 FOREIGN KEY ( equipo_nombre1 )
        REFERENCES equipo ( nombre )
    NOT DEFERRABLE;

ALTER TABLE equipo
    ADD CONSTRAINT equipo_empleado_fk FOREIGN KEY ( empleado_codigo,
                                                    empleado_equipo_nombre2 )
        REFERENCES empleado ( codigo,
                              equipo_nombre1 )
    NOT DEFERRABLE;

ALTER TABLE oficial
    ADD CONSTRAINT oficial_equipo_fk FOREIGN KEY ( nombre )
        REFERENCES equipo ( nombre )
    NOT DEFERRABLE;

ALTER TABLE ubd4432.piloto
    ADD CONSTRAINT piloto_equipo_fk FOREIGN KEY ( equipo_nombre )
        REFERENCES equipo ( nombre )
    NOT DEFERRABLE;

ALTER TABLE tiempos
    ADD CONSTRAINT relation_6_piloto_fk FOREIGN KEY ( piloto_nombre )
        REFERENCES ubd4432.piloto ( nombre )
    NOT DEFERRABLE;

ALTER TABLE tiempos
    ADD CONSTRAINT relation_6_tramo_fk FOREIGN KEY ( tramo_codigo )
        REFERENCES tramo ( codigo )
    NOT DEFERRABLE;

ALTER TABLE piloto_piloto
    ADD CONSTRAINT relation_8_piloto_fk FOREIGN KEY ( piloto_nombre )
        REFERENCES ubd4432.piloto ( nombre )
    NOT DEFERRABLE;

ALTER TABLE piloto_piloto
    ADD CONSTRAINT relation_8_piloto_fkv1 FOREIGN KEY ( piloto_nombre1 )
        REFERENCES ubd4432.piloto ( nombre )
    NOT DEFERRABLE;

ALTER TABLE tramo
    ADD CONSTRAINT tramo_circuito_fk FOREIGN KEY ( circuito_nombre )
        REFERENCES circuito ( nombre )
    NOT DEFERRABLE;



-- Informe de Resumen de Oracle SQL Developer Data Modeler: 
-- 
-- CREATE TABLE                            11
-- CREATE INDEX                             6
-- ALTER TABLE                             29
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
-- CREATE TABLESPACE                        1
-- CREATE USER                              1
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
-- WARNINGS                                 1
