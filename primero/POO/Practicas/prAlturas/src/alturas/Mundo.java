package alturas;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.*;

public class Mundo {
  List<Pais> paises;
  public Mundo(){
    paises = new ArrayList<>();
  }
  public static <K,V> void presentaEnPW(PrintWriter pw, Map<K,V> map){
      for(Map.Entry<K,V> entry : map.entrySet()){
        pw.println(entry.getKey() + "\t" + entry.getValue());
      }
  }
  public static <K,V> void presentaEnConsola(Map<K,V> map){
    PrintWriter pw = null;
    try{
      pw = new PrintWriter(System.out, true);
      presentaEnPW(pw,map);
    }finally {
      if(pw != null){
        pw.flush();
      }
    }

  }
  public List<Pais> getPaises(){
    return paises;
  }
  public void cargar(String file) throws IOException{
    try(Scanner sc = new Scanner(Path.of(file))){
      paises = new ArrayList<>();
      while(sc.hasNextLine()){
        String linea = sc.nextLine();
        try{
          String[] lista = linea.split("\\s*[,]\\s*");
          String nombre = lista[0];
          String continente = lista[1];
          double altura = Double.parseDouble(lista[2]);
          Pais nuevoPais = new Pais(nombre, continente, altura);
          paises.add(nuevoPais);
        }catch (Exception e){}

      }
    }
  }
  public SortedMap<String,Integer> numeroDePaisesPorContinente(){
    SortedMap<String, Integer> lista = new TreeMap<>();
    for(Pais p : paises){
      String cont = p.getContinente();
      Integer valor = lista.get(cont);
      if(valor == null){
        valor = 0;
      }
      lista.put(cont, valor + 1);
    }
    return lista;
  }

  public SortedMap<Double, List<Pais>> paisesPorAltura(){
    SortedMap<Double, List<Pais>> lista = new TreeMap<>();
    List<Pais> paisesOrdenados = paises;
    paisesOrdenados.sort(null);

    for(Pais pais : paisesOrdenados){
      double altura = (int)(pais.getAltura()*10)/10.0;

      if(lista.get(altura) == null){
        lista.put(altura, new ArrayList<Pais>());
      }

      lista.get(altura).add(pais);
    }
    return lista;
  }

  public SortedMap<String,List<Pais>> paisesPorContinente(){
    SortedMap<String, List<Pais>> lista = new TreeMap<>();
    List<Pais> paisesOrdenados = paises;
    paisesOrdenados.sort(null);

    for(Pais pais : paisesOrdenados){
      String cont = pais.getContinente();

      if(lista.get(cont) == null){
        lista.put(cont, new ArrayList<Pais>());
      }

      lista.get(cont).add(pais);
    }
    return lista;
  }

  public SortedMap<Character,List<Pais>> paisesPorInicial(){
    SortedMap<Character, List<Pais>> lista = new TreeMap<>();
    List<Pais> paisesOrdenados = paises;
    paisesOrdenados.sort(null);

    for(Pais pais : paisesOrdenados){
      Character letra = pais.getNombre().charAt(0);

      if(lista.get(letra) == null){
        lista.put(letra, new ArrayList<Pais>());
      }

      lista.get(letra).add(pais);
    }
    return lista;
  }

  public SortedMap<String,Double> mediaPorContinente(){
    SortedMap<String, Double> listaContinentes = new TreeMap<>();
    SortedMap<String,List<Pais>> paisesPorCont = paisesPorContinente();

    for(Map.Entry<String,List<Pais>> entrada : paisesPorCont.entrySet()){
      double media = 0;
      for(Pais pais : entrada.getValue()){
        media += pais.getAltura();
      }
      media /= entrada.getValue().size();
      listaContinentes.put(entrada.getKey(), media);
    }
    return listaContinentes;
  }

  public List<String> continentesConMasPaises(){
    SortedMap<String, Integer> listaContadores = numeroDePaisesPorContinente();
    List<String> paises = new ArrayList<>();
    int max = 0;
    for(Map.Entry<String, Integer> entrada : listaContadores.entrySet()){
      if(entrada.getValue() > max){
        max = entrada.getValue();
      }
    }
    for(Map.Entry<String, Integer> entrada : listaContadores.entrySet()){
      if(entrada.getValue() == max){
        paises.add(entrada.getKey());
      }
    }
    return paises;
  }

  public SortedSet<Pais> paisesOrdenadosPorAltura(){
    SortedSet<Pais> paisesOrdenados = new TreeSet<>(new CompAltura());
    for(Pais p : paises){
      paisesOrdenados.add(p);
    }
    return paisesOrdenados;
  }

  public SortedMap<String,SortedSet<Pais>> paisesPorContinenteAltura(){
    SortedMap<String, SortedSet<Pais>> lista = new TreeMap<>();

    for(Pais pais : paises){
      String cont = pais.getContinente();

      if(lista.get(cont) == null){
        lista.put(cont, new TreeSet<>(new CompAltura()));
      }

      lista.get(cont).add(pais);
    }
    return lista;
  }

  public SortedMap<String,SortedSet<Pais>> paisesPorContinenteAlturaDec(){
    SortedMap<String, SortedSet<Pais>> lista = new TreeMap<>();
    Comparator<Pais> comparator = new CompAltura().reversed();

    for(Pais pais : paises){
      String cont = pais.getContinente();

      if(lista.get(cont) == null){
        lista.put(cont, new TreeSet<>(new CompAltura().reversed()));
      }

      lista.get(cont).add(pais);
    }
    return lista;
  }
}

