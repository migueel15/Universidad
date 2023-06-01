package sociedad;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.*;

public class Sociedad {
  private Set<Socio> membersNoInActivities;
  private Map<String, Set<Socio>> membersInActivities;

  public Sociedad(){
    this.membersInActivities = new HashMap<String, Set<Socio>>();
    this.membersNoInActivities = new HashSet<>();
  }

  public void nuevoSocio(Socio socio){
    boolean existe = false;
    for(Map.Entry<String, Set<Socio>> entrada : membersInActivities.entrySet()){
      if(entrada.getValue().contains(socio)){
        existe = true;
      }
    }
    if(!existe){
      membersNoInActivities.add(socio);
    }
  }
  private void procesar(String linea){
    String[] lista = linea.split("[%]");
    Set<String> intereses = new HashSet<>();
    String[] inter = lista[1].split("[,]");
    for(String val : inter){
      intereses.add(val);
    }
    Socio socio = new Socio(lista[0], intereses, Integer.parseInt(lista[3]));
    nuevoSocio(socio);
  }
  public void leerDefichero(String file) {
    try(Scanner sc = new Scanner(Path.of(file))){
      while (sc.hasNext()){
        try{
          String linea = sc.nextLine();
          procesar(linea);
        }catch (Exception e){
          //
        }
      }
    }catch (IOException e){
      throw new SociedadException("File not found");
    }
  }
  public Set<Socio> inscritos(String actividad){
    return membersInActivities.get(actividad.toLowerCase());
  }
  protected Socio buscarSocioEnConjunto(Socio socio, Set<Socio> socios){
    boolean found = false;
    Socio socioDevuelto = null;
    Iterator<Socio> it = socios.iterator();
    while(it.hasNext() && !found){
      Socio s = it.next();
      if(s.equals(socio)){
        socioDevuelto = s;
        found = true;
      }
    }
    return socioDevuelto;
  }
  public void inscribir(String nombre, int identificador, String actividad){
    Set<String> intereses = new HashSet<>();
    Socio socioNuevo = new Socio(nombre,intereses,identificador);
    socioNuevo = buscarSocioEnConjunto(socioNuevo,membersNoInActivities);
    if(socioNuevo != null){
      Set<Socio> sociosActi = membersInActivities.get(actividad.toLowerCase())
      membersNoInActivities.remove(socioNuevo);
      sociosActi.add(socioNuevo);
      membersInActivities.put(actividad.toLowerCase(),sociosActi);
    }
  }
  public void guardarSocios(String file){
    try(PrintWriter pw = new PrintWriter(file)){
      guardarSocios(pw);
    }catch (IOException e){
      throw new SociedadException("File not found");
    }
  }
  public void guardarSocios(PrintWriter pw){
    for(Socio s : membersNoInActivities){
      pw.println(s.toString());
    }
    pw.flush();
  }

}
