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
select * from departamentos D, profesores P, 
        (select min(fecha_nacimiento) "fecha", departamento from profesores 
        group by departamento) E        

;


select * from sol_3_7






























