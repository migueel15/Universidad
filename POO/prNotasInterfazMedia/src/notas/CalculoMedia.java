package notas;

import java.util.List;

public interface CalculoMedia {
  public double calcula(List<Alumno> alumnos) throws AlumnoException;
}
