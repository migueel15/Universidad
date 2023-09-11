package indices;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public abstract class IndiceAbstracto implements Indice{
  protected List<String> texto;
  public IndiceAbstracto(){
    texto = new ArrayList<>();
  }

  @Override
  public void agregarFrase(String linea) {
    texto.add(linea);
  }
}
