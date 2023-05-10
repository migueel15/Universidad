package indices;

import java.io.PrintWriter;

public interface Indice {
  public void agregarFrase(String linea);
  public void resolver(String delimitadores);
  public void presentarIndice(PrintWriter pw);
  default void presentarIndiceConsola(){
    presentarIndice(new PrintWriter(System.out, true));
  }
}

