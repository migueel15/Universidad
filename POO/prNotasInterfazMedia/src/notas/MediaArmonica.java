package notas;

import java.util.List;

public class MediaArmonica implements CalculoMedia {
  public MediaArmonica(){}
  public double calcula(List<Alumno> alumnos) throws AlumnoException{
    int k = alumnos.size();

    // Check alumnos number
    if (k <= 0){
      throw new AlumnoException("No hay alumnos");
    }

    // calc Med.Arit
    double denominador = 0;
      for(Alumno al : alumnos){
        if(al.getCalificacion() > 0) {
          denominador += (1/al.getCalificacion());
        }
      }

    if(denominador == 0){
      throw new AlumnoException("No hay alumnos");
    }

    return k/denominador;
  }
}
