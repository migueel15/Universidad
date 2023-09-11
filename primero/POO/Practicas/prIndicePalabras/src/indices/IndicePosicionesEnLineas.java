package indices;

import java.io.PrintWriter;
import java.util.*;

public class IndicePosicionesEnLineas extends IndiceAbstracto{
  private SortedMap<String, SortedMap<Integer,SortedSet<Integer>>> indice;

  public IndicePosicionesEnLineas(){
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
        int contadorPalabra = 0;
        while(sc.hasNext()){
          contadorPalabra++;
          String palabra = sc.next();
          palabra = palabra.toLowerCase();
          SortedMap<Integer,SortedSet<Integer>> lineas = indice.get(palabra);
          if(lineas == null) {
            indice.put(palabra, new TreeMap<>());
          }
          if(indice.get(palabra).get(contadorLinea) == null){
            indice.get(palabra).put(contadorLinea, new TreeSet<Integer>());
            indice.get(palabra).get(contadorLinea).add(contadorPalabra);
          }else{
          indice.get(palabra).get(contadorLinea).add(contadorPalabra);
          }
        }
      }
    }
  }

  @Override
  public void presentarIndice(PrintWriter pw) {
    for(Map.Entry<String, SortedMap<Integer, SortedSet<Integer>>> entrada : indice.entrySet()){
      String palabra = entrada.getKey();
      pw.println(palabra);
      SortedMap<Integer, SortedSet<Integer>> lineaPosiciones = entrada.getValue();
      for(Map.Entry<Integer, SortedSet<Integer>> entradaPalabra: lineaPosiciones.entrySet()){
        int fila = entradaPalabra.getKey();
        Integer[] posiciones = entradaPalabra.getValue().toArray(new Integer[0]);
        StringJoiner sj = new StringJoiner(",", "<", ">");
        for(Integer numero : posiciones){
          sj.add(numero.toString());
        }
        pw.println("\t" + fila + " " + sj.toString());
      }
    }
  }
}
