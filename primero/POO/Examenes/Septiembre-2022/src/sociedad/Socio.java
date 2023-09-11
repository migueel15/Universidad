package sociedad;

import java.util.*;

public class Socio implements Comparable {
  private String name;
  private Set<String> interests;
  private int ident;
  public Socio(String name, Set<String> interests, int ident){
    if(name == null || name == ""){
      throw new SociedadException("Nombre no válido");
    }
    if(ident <= 0){
      throw new SociedadException("Identificador no válido");
    }
    this.name = name;
    this.interests = new HashSet<>();
    Iterator<String> it = interests.iterator();
    while (it.hasNext()){
      String actual = it.next().toLowerCase();
      this.interests.add(actual);
    }
    this.ident = ident;
  }

  public String getNombre() {
    return name;
  }
  public int getIdentificador() {
    return ident;
  }
  public Set<String> getIntereses() {
    return interests;
  }

  @Override
  public boolean equals(Object obj) {
    boolean igual = false;
    if(obj instanceof Socio){
      Socio otro = (Socio) obj;
      igual = this.name.equalsIgnoreCase(otro.name) && (this.ident == otro.ident);
    }
    return igual;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.name.toLowerCase(), this.ident);
  }

  @Override
  public String toString() {

    return "[" + name + ", " + interests.toString() + ", " + ident + "]";
  }
  @Override
  public int compareTo(Object o) {
    int posicion = 0;
    if(o instanceof Socio){
      Socio otro = (Socio) o;
      posicion = this.name.compareToIgnoreCase(otro.name);
      if(posicion == 0){
        posicion = Integer.compare(this.ident, otro.ident);
      }
    }
  return posicion;
  }
}
