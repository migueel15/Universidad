package cuentapalabras;

import java.awt.print.PrinterException;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class ContadorPalabras {
  private List<PalabraEnTexto> palabras;

  public ContadorPalabras(){
    palabras = new ArrayList<>();
  }

  private int esta(String palabra){
    int indice = -1;
    boolean exite = false;
    PalabraEnTexto parametro = new PalabraEnTexto(palabra);
    for(int i = 0 ; i < this.palabras.size() && !exite; i++){
      if(palabras.get(i).equals(parametro)){
        exite = true;
        indice = i;
      }
    }
    return indice;
  }

  protected void incluye(String palabra){
    if(palabra != ""){
      int posicion = esta(palabra);

      if(posicion == -1){
        palabras.add(new PalabraEnTexto(palabra));
      }else{
        palabras.get(posicion).incrementa();
      }
    }
  }

  private void incluyeTodas(String linea, String del){
    String[] palabras = linea.split(del);
    for(String pal : palabras){
      incluye(pal);
    }
  }

  public void incluyeTodas(String[] texto, String del){
    for(String linea : texto){
      incluyeTodas(linea, del);
    }
  }

  public void incluyeTodasFichero(String nomFich, String del) throws IOException {
    try(BufferedReader breader = Files.newBufferedReader(Path.of(nomFich))){
      String linea = breader.readLine();
      while(linea != null){
        incluyeTodas(linea, del);
        linea = breader.readLine();
      }
    }
  }

  public PalabraEnTexto encuentra(String palabra){
    boolean existe = false;
    PalabraEnTexto palabraABuscar = new PalabraEnTexto(palabra);
    for(PalabraEnTexto pal : palabras){
      if(pal.equals(palabraABuscar)) {
        existe = true;
        palabraABuscar = pal;
      }
    }
    if(!existe){
      throw new NoSuchElementException("No existe la palabra " + palabra);
    }
    return palabraABuscar;
  }

  @Override
  public String toString() {
    StringJoiner sj = new StringJoiner(" - ", "[", "]");
    for(PalabraEnTexto pal : palabras){
      sj.add(pal.toString());
    }

    return sj.toString();
  }

  public void presentaPalabras(PrintWriter pw){
    for(PalabraEnTexto palabra : palabras){
      pw.println(palabra.toString());
    }
  }

  public void presentaPalabras(String fichero) throws FileNotFoundException {
    try(PrintWriter pw = new PrintWriter(fichero)){
      presentaPalabras(pw);
    }
  }
}

/*
public Persona buscarPersona(Collection<Persona> c, Persona p){
  Iterator<Persona> it = c.iterator();
  boolean exist = false;
  Persona pActual;
  while(it.hasNext() && !exist){
    pActual = it.next();
    if(pActual.equals(p)){
      exist = true;
    }
  }
  return exist ? pActual : null;
}
*/

















