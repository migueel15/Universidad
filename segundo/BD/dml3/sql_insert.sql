CREATE TABLE MISALUMNOS (
"DNI" NUMBER(9,0) PRIMARY KEY,
"NOMBRE" VARCHAR2(20 BYTE),
"APELLIDO1" VARCHAR2(20 BYTE),
"APELLIDO2" VARCHAR2(20 BYTE),
"GENERO" VARCHAR2(4 BYTE),
"EMAIL" VARCHAR2(40 BYTE),
"FECHA_NACIMIENTO" DATE,
"FECHA_PRIM_MATRICULA" DATE
);

--1
insert into misalumnos 
(select a.dni,a.nombre,a.apellido1,a.apellido2,a.genero,a.email,a.fecha_nacimiento,a.fecha_prim_matricula 
from alumnos A,provincia P 
where a.cpro = p.codigo 
and upper(p.nombre) 
like 'MÁLAGA');

--2
update misalumnos
set nombre = upper(nombre),
apellido1 = upper(apellido1),
apellido2 = upper(apellido2);

--3
CREATE TABLE mimatricular AS SELECT * FROM DOCENCIA.MATRICULAR;

--4
delete from mimatricular M
where not exists (select * from misalumnos A where a.dni = m.alumno);

--5
insert into mimatricular(alumno, asignatura, grupo, curso) 
(
select m.alumno "ALUMNO",112 ASIGNATURA, 'A' GRUPO, '22/23' CURSO from misalumnos A, matricular M
where months_between(sysdate, a.fecha_prim_matricula) /12 < 2
and a.dni = m.alumno
and calificacion not in ('AP','MH','SB','NT')
)
; 

--6
update mimatricular
set calificacion = 'NP'
where
curso < '22/23'
and calificacion is null
;

--7
update mimatricular
set calificacion = 'AP'
where alumno in (select alumno from mimatricular where calificacion not in ('AP','MH','SB','NT') and curso < '22/23' group by alumno having count(*) < 2)
and curso < '22/23'
and calificacion not in ('AP','MH','SB','NT')
;

--8
insert into mimatricular (alumno,asignatura,grupo,curso)
select a.dni ALUMNO, asi.codigo ASIGNATURA, 'A' GRUPO, '22/23' CURSO from misalumnos a, asignaturas ASI
where (a.dni,asi.codigo) not in 
( -- alumnos y sus asignaturas ya matriculadas
select alumno,codigo from mimatricular, asignaturas
where asignatura = codigo
)
and a.dni not in 
( -- alumnos no válidos (aignaturas suspensas)
select m.alumno from misalumnos A,mimatricular M
where a.dni = m.alumno
and m.calificacion not in ('AP','MH','SB','NT')
and m.curso < '22/23'
)
and curso = 3
;

--9
delete from misalumnos
where dni in
(
select dni from alumnos
where email is null
and months_between(sysdate,fecha_nacimiento) / 12 > 23
);

--10
delete from misalumnos
where dni in
(select alumno from mimatricular
where curso in ('20/21','21/22')
and calificacion is not null 
and calificacion not in ('AP','MH','SB','NT')
group by alumno
having count(*) > 3)
;

--11
insert into DOCENCIA.V_MATRICULAR_EJERCICIO
select * from mimatricular;

--12
select * from DOCENCIA.V_MATRICULAR_EJERCICIO

--13
commit;

























