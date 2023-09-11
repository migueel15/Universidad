package indices;

import java.io.PrintWriter;
import java.util.*;

public class IndiceLineas extends IndiceAbstracto{
  private SortedMap<String, SortedSet<Integer>> indice;
  public IndiceLineas(){
    super();
    indice = new TreeMap<>();
  }

  @Override
  public void resolver(String delimitadores) {
    indice.clear();
    int contadorLinea = 0;
    for(String linea : texto){
      contadorLinea++;
      try(Scanner sc = new Scanner(linea)){
        sc.useDelimiter(delimitadores);
        while(sc.hasNext()){
          String palabra = sc.next();
          palabra = palabra.toLowerCase();
          SortedSet<Integer> lineas = indice.get(palabra);
          if(lineas == null){
            indice.put(palabra, new TreeSet<>());
            indice.get(palabra).add(contadorLinea);
          }else{
            indice.get(palabra).add(contadorLinea);
          }
        }
      }
    }
  }

  @Override
  public void presentarIndice(PrintWriter pw) {
    for(Map.Entry<String,SortedSet<Integer>> entrada : indice.entrySet()){
      String clave = entrada.getKey();
      Object[] valores = entrada.getValue().toArray();
      StringJoiner sj = new StringJoiner(",", "<", ">");
      for(Object numero : valores){
        sj.add(numero.toString());
      }
      pw.println(clave + "\t" + sj.toString());
    }
  }
}
