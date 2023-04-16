package notas;

import java.util.List;

public class MediaAritmetica implements CalculoMedia{
  public MediaAritmetica(){}
  public double calcula(List<Alumno> alumnos) throws AlumnoException{
    int n = alumnos.size();

    // Check alumnos number
    if (n <= 0){
      throw new AlumnoException("No hay alumnos");
    }

    // calc Med.Arit
    double media = 0;
    for(Alumno al : alumnos){
      media += al.getCalificacion();
    }
    media /= alumnos.size();

    return media;
  }
}