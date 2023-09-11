package nombres;

import java.util.Objects;

public class Nombre implements Comparable {
  char genero;
  String nombre;
  public Nombre(char genero, String nombre){
    if(nombre == "" || nombre == null){
      throw new RegistroCivilException("Nombre no válido");
    }
    if(genero != 'F' && genero != 'M'){
      throw new RegistroCivilException("Género no válido");
    }
    this.genero = genero;
    this.nombre = nombre;
  }

  public char getGenero() {
    return genero;
  }

  public String getNombre() {
    return nombre;
  }

  @Override
  public boolean equals(Object obj) {
    boolean iguales = false;
    if(obj instanceof Nombre){
      Nombre otro = (Nombre)obj;
      iguales = this.nombre.equalsIgnoreCase(otro.nombre) && this.genero == otro.genero;
    }
    return iguales;
  }

  @Override
  public int hashCode() {
    return Objects.hash(nombre.toLowerCase(), genero);
  }

  @Override
  public int compareTo(Object o) {
    int orden = 0;
    if(o instanceof Nombre){
      Nombre otro = (Nombre) o;
      orden = this.nombre.compareTo(otro.nombre);
      if(orden == 0){
        orden = Character.compare(this.genero, otro.genero);
      }
    }
    return orden;
  }

  @Override
  public String toString() {
    return "(" + nombre + ", " + genero + ")";
  }
}
