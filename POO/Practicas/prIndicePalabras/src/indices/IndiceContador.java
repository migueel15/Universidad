package indices;

import java.io.PrintWriter;
import java.util.Map;
import java.util.Scanner;
import java.util.SortedMap;
import java.util.TreeMap;

public class IndiceContador extends IndiceAbstracto{
  private SortedMap<String,Integer> indice;
  public IndiceContador(){
    super();
    indice = new TreeMap<>();
  }

  @Override
  public void resolver(String delimitadores) {
    indice.clear();
    for(String linea : texto){
      try(Scanner sc = new Scanner(linea)){
        sc.useDelimiter(delimitadores);
        while(sc.hasNext()){
          String palabra = sc.next();
          palabra = palabra.toLowerCase();
          Integer contador = indice.get(palabra);
          if(contador == null){
            indice.put(palabra,1);
          }else{
            indice.put(palabra, contador+1);
          }
        }
      }
    }
  }

  @Override
  public void presentarIndice(PrintWriter pw) {
    for(Map.Entry<String,Integer> entrada : indice.entrySet()){
      String clave = entrada.getKey();
      int valor = entrada.getValue();
      pw.println(clave + "\t" + valor);
    }
  }
}
