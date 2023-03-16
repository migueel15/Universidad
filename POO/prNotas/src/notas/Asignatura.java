package notas;

import java.util.List;

public class Asignatura {
  private String nombre;
  private List<String> errores;
  private List<String> alumnos;

  public Asignatura(String asignatura, String[]alumnos){
    this.nombre = asignatura;

    for(String alumno : alumnos){
      String dni;
      String nombre;
      double nota;

      String[] datos = alumno.split("\\\\s*[;]\\\\s*");
      int paramLength = datos.length;
      if(paramLength == 2){
        try {
          Alumno user = new Alumno(datos[0], datos[1]);
        }catch ()
      }

    }

  }
}
