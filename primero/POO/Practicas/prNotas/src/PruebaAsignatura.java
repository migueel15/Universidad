import notas.Alumno;
import notas.AlumnoException;
import notas.Asignatura;

public class PruebaAsignatura {
  public static void main(String[] args) throws AlumnoException {
    String[] alumnos = {
        "12455666F;Lopez Perez, Pedro;6.7",
        "33678999D;Merlo Gomez, Isabel;5.8",
        "23555875G;Martinez Herrera, Lucia;9.1"
    };
    Asignatura asignatura = new Asignatura("POO",alumnos);

    try {
      System.out.println(asignatura.getMedia());
    }catch (AlumnoException e){
      System.out.println(e.getMessage());
    }

    for(Alumno al : asignatura.getAlumnos()){
      System.out.println(al.getDni());
    }

    try {
      System.out.println(asignatura.getCalificacion(asignatura.getAlumnos().get(0)));
      System.out.println(asignatura.getCalificacion(new Alumno("12455666F", "Lopez Lopez, Pedro")));
    } catch (AlumnoException e){
      System.out.println(e.getMessage());
    }


  }
}
