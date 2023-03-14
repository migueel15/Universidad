package libreria;

import java.lang.reflect.Array;
import java.util.Arrays;

public class OfertaAutor implements OfertaFlex{
  private double porcDescuento;
  private String[] autoresOferta;

  public OfertaAutor(double porcDescuento, String[] autoresOferta){
    this.porcDescuento = porcDescuento;
    this.autoresOferta = autoresOferta;
  }

  private int buscarAutorOferta(String autor){
    int index = -1;
    for (int i = 0 ; i < autoresOferta.length && index == -1; i++){
      if(autoresOferta[i].equalsIgnoreCase(autor)){
        index = i;
      }
    }
    return index;
  }
  @Override
  public double getDescuento(Libro libro){
    return buscarAutorOferta(libro.getAutor()) != -1 ? porcDescuento : 0;
  }

  @Override
  public String toString(){
    return porcDescuento + "%" + Arrays.toString(autoresOferta);
  }

}
