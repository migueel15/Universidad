package nombres;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

public class RegistroCivil {
  String estado;
  SortedMap<Nombre,SortedMap<Integer,Integer>> registro;

  public RegistroCivil(String estado, String fichero) throws IOException {
    this.estado = estado;
    registro = new TreeMap<>();
    leerFichero(fichero);

  }

  private void agregar(Nombre nombre, int year, int repet){
    SortedMap<Integer,Integer> values = registro.get(nombre);
    if(values == null){
      values = new TreeMap<>();
    }
    registro.get(nombre).put(year, repet);
  }

  private void procesar(String linea){
    String[] datos = linea.split(";");
    String estado = datos[0];

    if(this.estado.equals(estado)){
      char genero = datos[1].charAt(0);
      int year = Integer.parseInt(datos[2]);
      String nombreValor = datos[3];
      int repeticiones = Integer.parseInt(datos[4]);

      Nombre nombre = new Nombre(genero, nombreValor);
      agregar(nombre,year, repeticiones);
    }
  }

  private void leerFichero(String file) throws IOException {
    try(Scanner sc = new Scanner(Path.of(file))) {
      while(sc.hasNextLine()){
        try{
          String linea = sc.nextLine();
          procesar(linea);
        }catch (Exception e){
          // ---------- //
        }
      }
    }catch (IOException e){
      throw new RegistroCivilException("Archivo no encontrado");
    }
  }

  public String getEstado() {
    return estado;
  }
  public Set<Nombre> getNombres(){
    Set<Nombre> lista = new HashSet<>(registro.keySet());
    return lista;
  }

  public SortedSet<String> selecciona(Filtro filtro){
    TreeSet<String> nombres = new TreeSet<>();

    for(Nombre nombre : getNombres()){
      if(filtro.criterio(nombre)){
        nombres.add(nombre.getNombre());
      }
    }
    return nombres;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(estado + "\n");
    for(Map.Entry<Nombre,SortedMap<Integer,Integer>> entrada: registro.entrySet()){
      Nombre nombre = entrada.getKey();
      sb.append(nombre.toString() + ":\t" + entrada.getValue().toString() + "\n");
    }
    return sb.toString();
  }
}
