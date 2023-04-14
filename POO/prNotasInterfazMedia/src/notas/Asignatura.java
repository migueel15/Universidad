package notas;

import java.util.ArrayList;
import java.util.List;

public class Asignatura {
  private String nombre;
  private List<String> errores = new ArrayList<>();
  private List<Alumno> alumnos = new ArrayList<>();

  public Asignatura(String asignatura, String[]alumnosString){
    this.nombre = asignatura;

    for(String alumno : alumnosString){
      Alumno user;
      String[] datos = alumno.split("\\s*[;]\\s*");
      int paramLength = datos.length;

      if(paramLength == 3){
        try {
          user = new Alumno(datos[0], datos[1], Double.parseDouble(datos[2]));
          alumnos.add(user);
        } catch (NumberFormatException e){
          String error = "ERROR. Calificación no numérica: " + alumno;
          errores.add(error);
        } catch (AlumnoException e){
          String error = "ERROR. Calificación negativa: " + alumno;
          errores.add(error);
        }
      }

      if(paramLength < 3){
        String error = "ERROR. Faltan datos: " + alumno;
        errores.add(error);
      }
    }
  }

  public double getCalificacion(Alumno alumno) throws AlumnoException{
    boolean existe = false;
    int indice = -1;
    for (int i = 0; i < alumnos.size() && !existe; i++){
      if(alumno.equals(alumnos.get(i))){
        existe = true;
        indice = i;
      };
    }
    if(!existe){
      throw new AlumnoException("El alumno " + alumno.getNombre() + " " + alumno.getDni()
      + " no se encuentra");
    }

    return alumnos.get(indice).getCalificacion();
  };

  public String getNombre() {
    return nombre;
  }

  public List<Alumno> getAlumnos() {
    return alumnos;
  }

  public List<String> getErrores() {
    return errores;
  }

  @Override
  public String toString() {
    StringBuilder st = new StringBuilder(this.nombre);
    st.append(": ").append("{ ").append(alumnos).append(",").append(errores).append(" }");

    return st.toString();
  }

  public double getMedia() throws AlumnoException{
    if(alumnos.size() == 0){
      throw new AlumnoException("No hay alumnos");
    }

    double totalNotas = 0;
    for(Alumno alumno : alumnos){
      totalNotas += alumno.getCalificacion();
    }
    return totalNotas / (alumnos.size());
  }

  public double getMedia(CalculoMedia calc) throws AlumnoException{
    return calc.calcula(getAlumnos());
  }
}
