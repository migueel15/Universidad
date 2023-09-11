package notas;

import java.util.Objects;

public class Alumno {
  private String dni;
  private String nombre;
  private double calificacion;

  public Alumno(String dni, String nombre, double calificacion) throws AlumnoException {
    if(calificacion < 0){
      throw new AlumnoException("Calificación negativa");
    }
    this.dni = dni;
    this.nombre = nombre;
    this.calificacion = calificacion;
  }

  public Alumno(String dni, String nombre){
    this.dni = dni;
    this.nombre = nombre;
    this.calificacion = 0;
  }

  public String getNombre() {
    return nombre;
  }

  public String getDni() {
    return dni;
  }

  public double getCalificacion() {
    return calificacion;
  }

  @Override
  public String toString() {
    return nombre + " " + dni;
  }

  @Override
  public boolean equals(Object obj){
    boolean igual = false;
    if(obj instanceof Alumno){
      Alumno alumno = (Alumno)obj;
      igual =  this.getNombre().equals(alumno.getNombre()) &&
          this.getDni().equalsIgnoreCase(alumno.getDni());
    }
    return igual;
  }

  @Override
  public int hashCode(){
    return Objects.hash(this.dni.toLowerCase(), this.nombre);
  }
}