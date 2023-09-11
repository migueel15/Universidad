package notas;

import java.util.ArrayList;
import java.util.List;

public class MediaSinExtremos implements CalculoMedia {
  double min;
  double max;
  public MediaSinExtremos(double min, double max){
    this.min = min;
    this.max = max;
  }

  public double getMin() {
    return min;
  }

  public double getMax() {
    return max;
  }

  public void setMin(double min) {
    this.min = min;
  }

  public void setMax(double max) {
    this.max = max;
  }

  public double calcula(List<Alumno> alumnos) throws AlumnoException{
    List<Alumno> alumnosFiltro = new ArrayList<>();
    for(Alumno al : alumnos){
      if(al.getCalificacion() >= min && al.getCalificacion() <= max){
        alumnosFiltro.add(al);
      }
    }
    if(alumnosFiltro.size() == 0){
      throw new AlumnoException("No hay alumnos");
    }

    return new MediaAritmetica().calcula(alumnosFiltro);
  }
}
