--1
select min(d.nombre),count(*) as "Numero de profesores" from profesores P 
join departamentos D on (p.departamento = d.codigo)
group by p.departamento;

--2
select min(d.nombre) "Nombre",sum(a.creditos) "Suma creditos" from asignaturas A
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
select min(p.nombre) "PROVINCIA" ,sum(m.hombres+m.mujeres) "SUMA" 
from municipio M
join provincia P on (m.cpro = p.codigo)
group by cpro;

--7
select d.nombre, p.nombre || ' ' || p.apellido1 
from departamentos D, profesores P, (select min(fecha_nacimiento) "FECHA", departamento from profesores group by departamento) E 
where p.fecha_nacimiento = e.fecha 
and d.codigo = e.departamento;


--8
select distinct a.dni,asi.codigo,asi.nombre from alumnos A, matricular M, asignaturas ASI
where 
a.dni = m.alumno
and m.asignatura = asi.codigo
and
(a.dni,asi.creditos) in
(select m.alumno, max(a.creditos) from matricular M, asignaturas A
where m.asignatura = a.codigo
group by m.alumno)
;

--9
select d.nombre, p.nombre || ' ' || p.apellido1 from departamentos D, profesores P
where
p.departamento = d.codigo
and (p.antiguedad, p.departamento) in
(select min(antiguedad), departamento from profesores
group by departamento);

--10
select d.nombre DEPARTAMENTO, a.nombre ASIGNATURA from departamentos D, asignaturas A
where d.codigo = a.departamento
and (a.creditos, a.departamento) in
(
select min(creditos) CRED, departamento  from asignaturas
group by departamento
)
;

--11
select distinct a.nombre, al.nombre || ' ' || al.apellido1 || ' ' || al.apellido2, al.fecha_nacimiento from asignaturas A, alumnos AL, matricular M
where
a.codigo = m.asignatura
and al.dni = m.alumno
and (m.asignatura ,al.fecha_nacimiento) in
(
    select asignatura, max(fecha_nacimiento) FECHA
    from alumnos A, matricular M
    where a.dni = m.alumno
    and m.curso = '20/21'
    group by asignatura
)

;

select * from sol_3_11;



















































