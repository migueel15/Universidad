-- 1
select P.nombre,apellido1,apellido2 from profesores P join departamentos D
on (P.departamento = D.codigo) where D.nombre like 'Lenguajes y Ciencias de la Computacion';

--2
select distinct D.codigo,D.nombre,nvl(to_char(D.practicos),'No tiene') from alumnos A, asignaturas D, matricular M
where A.dni = M.alumno and D.codigo = M.asignatura and A.nombre = 'Nicolas';

--3
select P.nombre,apellido1,apellido2,floor((sysdate - P.antiguedad)/7) "Antiguedad", 
to_char(P.antiguedad, 'DAY') "Se cumple una semanaa", 
case
    when to_char(p.antiguedad,'DAY') = to_char(sysdate,'DAY') then sysdate
    else next_day(sysdate,to_char(P.antiguedad, 'DAY'))
end as "Se cumple una semana"

from profesores P join departamentos D on (P.departamento = D.codigo)
where D.nombre like 'Ingenieria de Comunicaciones';

--4
select a.nombre,a.apellido1,a.apellido2,m.calificacion from alumnos A join matricular M on (a.dni = m.alumno) where m.calificacion not like 'SP' and m.asignatura = 112;

--5
select p.id,p.nombre,p.apellido1,p.apellido2,a.codigo, a.nombre from profesores P join impartir I on (p.id = i.profesor) join asignaturas A on (i.asignatura = a.codigo);

--6
select a1.nombre,trunc(months_between(sysdate,a1.fecha_nacimiento)/12),a2.nombre,trunc(months_between(sysdate,a2.fecha_nacimiento)/12)
from alumnos A1,alumnos A2 where upper(a1.apellido1) = upper(a2.apellido1) and a1.dni < a2.dni;

--7
select distinct upper(a1.apellido1) "Primer apellido", upper(a2.apellido1) "Segundo apellido" from alumnos a1, alumnos a2
where 
cast(to_char(a1.fecha_nacimiento,'YYYY') as int) BETWEEN 1998 and 1999 
and cast(to_char(a2.fecha_nacimiento,'YYYY') as int) BETWEEN 1998 and 1999
and a1.dni != a2.dni;

--8
select p1.nombre,p1.apellido1,p1.apellido2,p1.antiguedad, p2.nombre,p2.apellido1,p2.apellido2,p2.antiguedad from profesores P1, profesores P2 
where p1.departamento = p2.departamento
and abs(months_between(p1.antiguedad,p2.antiguedad)/12) <= 2
and p1.id < p2.id;

--9
select t2.nombre,t1.nombre from (select a1.nombre || ' ' || a1.apellido1 || ' ' || a1.apellido2 as nombre, a1.dni,a1.genero,a1.fecha_prim_matricula as fecha,m1.calificacion from alumnos a1 join matricular m1 on (a1.dni = m1.alumno)
where m1.asignatura = 112 and a1.genero = 'MASC') t1 join (select a1.nombre || ' ' || a1.apellido1 || ' ' || a1.apellido2 as nombre,a1.dni,a1.genero,a1.fecha_prim_matricula as fecha,m1.calificacion from alumnos a1 join matricular m1 on (a1.dni = m1.alumno)
where m1.asignatura = 112 and a1.genero = 'FEM') t2 on (t1.dni < t2.dni)
where
to_char(t1.fecha,'WW') = to_char(t2.fecha,'WW')
and DECODE(t1.calificacion, NULL, 0, 'SP', 1, 'AP', 2, 'NT', 3,
'SB', 4, 'MH', 5) < DECODE(t2.calificacion, NULL, 0, 'SP', 1, 'AP', 2, 'NT', 3,
'SB', 4, 'MH', 5);

--10
select a1.nombre,a2.nombre,a3.nombre, a1.cod_materia,a2.cod_materia,a3.cod_materia "Materia" from asignaturas a1,asignaturas a2,asignaturas a3 
where a1.cod_materia = a2.cod_materia 
and a1.cod_materia = a3.cod_materia
and a1.codigo < a2.codigo
and a2.codigo < a3.codigo





















