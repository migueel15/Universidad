--1
select min(d.nombre),count(*) as "Numero de profesores" from profesores P 
join departamentos D on (p.departamento = d.codigo)
group by p.departamento;

--2
select min(d.nombre),sum(a.creditos) "Suma creditos" from asignaturas A
join departamentos D on (a.departamento = d.codigo)
group by a.departamento;

--3
select count(distinct alumno) "ALUMNOS" from matricular group by curso;

--4
select p.despacho,sum(i.carga_creditos) from profesores P
join impartir I on(p.id = i.profesor and p.despacho is not null)
group by p.despacho;

--5
select m.asignatura, 
count(case when upper(a.genero) like 'FEM' then 1 end)/count(a.nombre) * 100 
from matricular M join alumnos A on (m.alumno = a.dni)
group by m.asignatura;

--6
select min(p.nombre) ,sum(m.hombres+m.mujeres) "SUMA" from municipio M
join provincia P on (m.cpro = p.codigo)

group by cpro;

--7
select D.nombre "DEPARTAMENTO", P.nombre || ' ' || p.apellido1 "PROFESOR" from departamentos D, profesores P, 
        (select min(fecha_nacimiento) "FECHA", departamento from profesores 
        group by departamento) E
where E.departamento = D.codigo and p.fecha_nacimiento = E.fecha;

--8
select h.alumno,h.codigo,h.nombre,h.creditos from

(select M.alumno, ASI.codigo, ASI.nombre, ASI.creditos from matricular M, asignaturas ASI
where m.asignatura = asi.codigo) H
join
(select m.alumno,max(asi.creditos) "CREDITOS" from matricular M,asignaturas ASI
where m.asignatura = asi.codigo
group by m.alumno) F
on(H.alumno = F.alumno and H.creditos = F.creditos)
;

--9
select D.nombre, L.profesor from departamentos D,
(select P.departamento, P.nombre || ' ' || p.apellido1 "PROFESOR" from
(select departamento, min(antiguedad) "ANTIGUEDAD" from profesores
group by departamento) H,
profesores P
where H.departamento = p.departamento and p.antiguedad = H.antiguedad) L
where D.codigo = L.departamento
;

--10
select D.nombre "DEPARTAMENTO", ASI.nombre "ASIGNATURA"  from departamentos D, asignaturas ASI,
(select departamento, min(creditos) "CREDITOS" from asignaturas
group by departamento) F
where D.codigo = F.departamento
and ASI.creditos = F.creditos
and ASI.departamento = D.codigo;

--11
select ASI.nombre "ASIGNATURA", A.nombre || ' ' || A.apellido1 "ALUMNO", A.fecha_nacimiento from matricular M, alumnos A, asignaturas ASI where
M.alumno = A.dni and M.asignatura = ASI.codigo and (ASI.codigo, A.fecha_nacimiento)
in
(select asignatura, min(fecha_nacimiento) "FECHA" from matricular join alumnos on (alumno = dni)
where curso like '20/21'
group by asignatura)
and M.curso like '20/21'
;

--12
select P.nombre || ' ' || P.apellido1 "PROFESOR", L.creditos from profesores P,
(select id, sum(carga_creditos) "CREDITOS" from profesores P join impartir I
on( P.id = I.profesor)
group by P.id
having sum(I.carga_creditos) = 
(select max(sum(carga_creditos))"CARGA" from impartir
group by profesor)
) L
where P.id = L.id
;

--13
select D.nombre from departamentos D, asignaturas A
where A.departamento = D.codigo
group by D.nombre
having count(A.nombre) =
(select max(count(*)) "NUMERO" from asignaturas group by departamento);


--14
select profesor, sum(carga_creditos) from impartir group by profesor
having sum(carga_creditos) < 10
;

--15
select P.nombre, P.apellido1, P.apellido2 from impartir I, profesores P
where I.profesor = P.id
group by P.nombre,P.apellido1,P.apellido2
having avg(I.carga_creditos) >
(select avg(media_ind) "MEDIA" from (select avg(carga_creditos) "MEDIA_IND" from
impartir group by profesor));

--16
select profesor "PROFESOR" from impartir
where carga_creditos < 6.5
and curso = '21/22'
group by profesor
having count(carga_creditos) >= 2

;
select * from sol_3_16;



























