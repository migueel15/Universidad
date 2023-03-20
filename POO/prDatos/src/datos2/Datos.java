package datos2;

import java.util.ArrayList;
import java.util.List;

public class Datos {
  private List<Double> datos;
  private List<String> errores;
  private double min;
  private double max;

  public Datos(String[] datos, double min, double max){
    this.datos = new ArrayList<>();
    this.errores = new ArrayList<>();
    for (String val : datos){
      try {
        this.datos.add(Double.parseDouble(val));
      }catch (NumberFormatException e){
        this.errores.add(val);
      }
    }
    this.min = min;
    this.max = max;
  }

  public double calcMedia() throws DatosException {
    double suma = 0;
    int elementos = 0;
    for (double val:datos){
      if(val <= max && val >= min){
        suma += val;
        elementos++;
      }
    }
    if (elementos == 0){
      throw new DatosException("No hay datos en el rango especificado");
    }
    return suma / elementos;
  }

  public double calcDesvTipica() throws DatosException {
    double media = calcMedia();
    double suma = 0;
    int elementos = 0;
    for(double val : datos){
      if(val <= max && val >= min){
        suma += Math.pow(val - media, 2);
        elementos++;
      }
    }
    return Math.sqrt(suma/elementos);
  }

  public void setRango(String newParams) throws DatosException {
    try {
      int indexcoma = newParams.indexOf(";");
      this.min = Double.parseDouble(newParams.substring(0,indexcoma));
      this.max = Double.parseDouble(newParams.substring(indexcoma+1));
    }catch (NumberFormatException | IndexOutOfBoundsException e){
      throw new DatosException("Error en los datos al establecer el rango");
    }
  }

  public List<Double> getDatos(){
    return List.copyOf(datos);
  }

  public List<String> getErrores(){
    return List.copyOf(errores);
  }

  @Override
  public String toString(){
    try{
      return "Min: " + min + ", Max: " + max + ", " + datos + ", " + errores
          + ", Media: " + calcMedia() + ", DesvTipica: " + calcDesvTipica();
    }catch (DatosException e){
      return "Min: " + min + ", Max: " + max + ", " + datos + ", " + errores
          + ", Media: " + "ERROR" + ", DesvTipica: " + "ERROR";
    }
  }
}