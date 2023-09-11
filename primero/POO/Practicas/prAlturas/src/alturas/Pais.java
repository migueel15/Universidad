package alturas;

import java.util.Objects;

public class Pais implements Comparable {
  private String nombre;
  private String continente;
  private double altura;
  public Pais(String n, String c, double a){
    this.nombre = n;
    this.continente = c;
    this.altura = a;
  }

  public String getNombre() {
    return nombre;
  }

  public String getContinente() {
    return continente;
  }

  public double getAltura() {
    return altura;
  }

  @Override
  public boolean equals(Object obj) {
    boolean igual = false;
    if(obj instanceof Pais){
      Pais otro = (Pais) obj;
      igual = this.nombre.equals(otro.nombre);
    }
   return igual;
  }

  @Override
  public int hashCode() {
    return Objects.hash(nombre);
  }

  @Override
  public int compareTo(Object o) {
    int order = 0;
    if(o instanceof Pais){
      Pais otro = (Pais) o;
      order = this.nombre.compareTo(otro.nombre);
    }else {
      throw new RuntimeException("Object instance not Pais");
    }
    return order;
  }

  @Override
  public String toString() {
    return "Pais(" + nombre + "," + continente + "," + altura + ")";
  }
}
