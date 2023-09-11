import notas.Alumno;
import notas.AlumnoException;

public class PruebaAlumno {
  public static void main(String[] args){
    try {
      Alumno alumno1 = new Alumno("22456784F", "Gonzalez Perez, Juan", 5.5);
      Alumno alumno2 = new Alumno("33456777S", "Gonzalez Perez, Juan", 3.4);

      System.out.println(" Nombre: " + alumno1.getNombre() + " Nota: " + alumno1.getCalificacion());
      System.out.println(" Nombre: " + alumno2.getNombre() + " Nota: " + alumno2.getCalificacion());

      System.out.println("Son iguales?: " + alumno1.equals(alumno2));

      alumno2 = new Alumno("33456777S", "Gonzalez Perez, Juan", -3.4);

      System.out.println(" Nombre: " + alumno1.getNombre() + " Nota: " + alumno1.getCalificacion());
      System.out.println(" Nombre: " + alumno2.getNombre() + " Nota: " + alumno2.getCalificacion());

      System.out.println(alumno1.equals(alumno2));
    }catch (AlumnoException e){
      System.out.println(e.getMessage());
    }
  }
}