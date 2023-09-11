package cuentapalabras;

public class PalabraEnTexto {
  private String palabra;
  private int veces;
  public PalabraEnTexto(String palabra){
    this.palabra = palabra.toUpperCase();
    this.veces = 1;
  }

  public void incrementa(){
    this.veces++;
  }

  @Override
  public String toString() {
    return palabra + ": " + veces;
  }

  @Override
  public int hashCode() {
    return java.util.Objects.hash(this.palabra);
  }

  @Override
  public boolean equals(Object otro) {
    boolean iguales = false;

    if(otro instanceof PalabraEnTexto){
      PalabraEnTexto otraPalabra = (PalabraEnTexto) otro;
      iguales = this.palabra.equals(otraPalabra.palabra);

    }

    return iguales;
  }
}
