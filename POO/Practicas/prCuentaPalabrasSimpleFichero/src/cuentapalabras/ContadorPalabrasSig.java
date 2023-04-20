package cuentapalabras;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.Buffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ContadorPalabrasSig extends ContadorPalabras{
  List<String> noSignificativas;

  public ContadorPalabrasSig(){
    noSignificativas = new ArrayList<>();
  }

  public void leeArrayNoSig(String[] array){
    noSignificativas.clear();
    for(String pal : array){
      if(pal != ""){
        noSignificativas.add(pal.toUpperCase());
      }
    }
  }

  private void anyadePalabrasNoSignificativas(String linea, String del){
    String[] lista = linea.split(del);
    for(String pal : lista){
      if(pal != ""){
        noSignificativas.add(pal.toUpperCase());
      }
    }
  }

  public void leeFicheroNoSig(String filNoSig, String del) throws IOException {
    try(BufferedReader breader = Files.newBufferedReader(Path.of(filNoSig))){
      noSignificativas.clear();
      String linea = breader.readLine();
      while(linea != null){
        anyadePalabrasNoSignificativas(linea, del);
        linea = breader.readLine();
      }
    }
  }

  private int estaNoSig(String palabra){
    int indice = -1;
    boolean exite = false;
    for(int i = 0 ; i < this.noSignificativas.size() && !exite; i++){
      if(noSignificativas.get(i).equalsIgnoreCase(palabra)){
        exite = true;
        indice = i;
      }
    }
    return indice;
  }

  @Override
  protected void incluye(String palabra) {
    if(estaNoSig(palabra) == -1){
      super.incluye(palabra);
    }
  }
}
