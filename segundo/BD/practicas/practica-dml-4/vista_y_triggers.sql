--1
CREATE TABLE TOUR AS SELECT * FROM DOCENCIA.TOUR2017;

--2
CREATE TABLE INFO_PERSONAL (
    ID NUMBER(3,0) PRIMARY KEY,
    NOMBRE VARCHAR2(50 BYTE),
    NACIONALIDAD VARCHAR2(3 BYTE)
    );
    
CREATE TABLE INFO_PROFESIONAL (
    ID NUMBER(3,0) PRIMARY KEY,
    EQUIPO VARCHAR2(50 BYTE),
    PAIS VARCHAR2(20 BYTE),
    CATEGORIA VARCHAR2 (20 BYTE)
    );
    
INSERT INTO INFO_PERSONAL(ID,NOMBRE,NACIONALIDAD)
SELECT ID,NAME,NATIONALITY FROM TOUR;

INSERT INTO INFO_PROFESIONAL(ID,EQUIPO,PAIS,CATEGORIA)
SELECT ID,TEAM,COUNTRY,CATEGORY FROM TOUR;

--3
CREATE VIEW TOUR_VIEW AS
SELECT * FROM TOUR;

--4
CREATE OR REPLACE TRIGGER TR_INSERTA_CICLISTA
INSTEAD OF INSERT ON TOUR_VIEW
FOR EACH ROW
BEGIN
    INSERT INTO INFO_PERSONAL(ID,NOMBRE,NACIONALIDAD)
    VALUES (:NEW.NAME,:NEW.NAME,:NEW.NATIONALITY);
    
    INSERT INTO INFO_PROFESIONAL(ID,EQUIPO,PAIS,CATEGORIA)
    VALUES (:NEW.ID, :NEW.TEAM, :NEW.COUNTRY, :NEW.CATEGORY);
END TR_INSERTA_CICLISTA;

--5
CREATE TABLE LOG_INSERCION(
    USUARIO VARCHAR2(255),
    FECHA DATE
    );

CREATE OR REPLACE TRIGGER TR_LOG_INSERCION
AFTER INSERT ON TOUR
BEGIN
    INSERT INTO LOG_INSERCION(USUARIO,FECHA)
    VALUES(USER,SYSDATE());
END TR_LOG_INSERCION;

--6
CREATE VIEW TOUR_SPAIN AS
SELECT * FROM TOUR WHERE UPPER(NATIONALITY) LIKE 'ESP';

CREATE VIEW TOUR_ITALY AS
SELECT * FROM TOUR WHERE UPPER(NATIONALITY) LIKE 'ITA';

--7
INSERT INTO TOUR_SPAIN(ID,TEAM,COUNTRY,NAME,NATIONALITY,CATEGORY)
VALUES(250,'EQUIPO-PRUEBA','Spain', 'NOMBRE-PRUEBA', 'Esp', 'CATEGORIA-PRUEBA');

--8
INSERT INTO TOUR_SPAIN(ID,TEAM,COUNTRY,NAME,NATIONALITY,CATEGORY)
VALUES(251,'EQUIPO-PRUEBA','Spain', 'NOMBRE-PRUEBA', 'Ita', 'CATEGORIA-PRUEBA');
















