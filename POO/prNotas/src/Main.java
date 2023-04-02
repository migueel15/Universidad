import notas.AlumnoException;
import notas.Alumno;
import notas.Asignatura;
public class Main {
  static final String[] als = {
      "25653443S;Garcia Gomez, Juan;8.1",
      "23322443K;Lopez Turo, Manuel;4.3",
      "24433522M;Merlo Martinez, Juana;5.3",
      "53553421D;Santana Medina, Petra;-7.1",
      "55343442L,Godoy Molina, Marina;6.3",
      "34242432J;Fernandez Vara, Pedro;2.k",
      "42424312G;Lopez Gama, Luisa;7.1" };
  public static void main(String[] args) {
    try {
      Asignatura algebra = new Asignatura("Algebra", als);
      try {
        Alumno al1 = new Alumno("23322443k", "Lopez Turo, Manuel");
        Alumno al2 = new Alumno("34242432J", "Fernandez Vara, Pedro");
        System.out.println("Calificacion de " + al1 + ": "
            + algebra.getCalificacion(al1));
        System.out.println("Calificacion de " + al2 + ": "
            + algebra.getCalificacion(al2));
      } catch (AlumnoException e) {
        System.err.println(e.getMessage());
      }
      try {
        System.out.printf("Media %4.2f\n", algebra.getMedia());
      } catch (AlumnoException e) {
        System.err.println(e.getMessage());
      }
      System.out.println("Alumnos...");
      for (Alumno alumno : algebra.getAlumnos()) {
        System.out.println(alumno + ": " + alumno.getCalificacion());
      }
      System.out.println("Errores...");
      for (String error : algebra.getErrores()) {
        System.out.println(error);
      }
      System.out.println("Asignatura...");
      System.out.println(algebra);
    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
  }
}