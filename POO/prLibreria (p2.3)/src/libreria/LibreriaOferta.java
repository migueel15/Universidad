package libreria;

import java.lang.reflect.Array;
import java.util.Arrays;

public class LibreriaOferta extends Libreria {
  private double porcDescuento;
  private String[] autoresOferta;

  public LibreriaOferta(double porcDescuento, String[]autoresOferta){
    super();
    this.porcDescuento = porcDescuento;
    this.autoresOferta = autoresOferta;
  }

  public void setOferta(double porcDescuento, String[]autoresOferta){
    this.porcDescuento = porcDescuento;
    this.autoresOferta = autoresOferta;
  }

  public String[] getOferta(){
    return Arrays.copyOf(autoresOferta,autoresOferta.length);
  }

  public double getDescuento(){
    return porcDescuento;
  }

  @Override
  public void addLibro(String autor, String titulo, double precioBase){
    boolean exist = false;
    for (int i = 0; i < autoresOferta.length && !exist; i++){
      if(autoresOferta[i].equalsIgnoreCase(autor)){
        exist = true;
      }
    }

    if(exist){
      LibroOferta libro = new LibroOferta(autor, titulo, precioBase, this.porcDescuento);
      anyadirLibro(libro);
    }else{
      Libro libro = new Libro(autor, titulo, precioBase);
      anyadirLibro(libro);
    }
  }

  @Override
  public String toString(){
    return(porcDescuento + "%"
        + Arrays.toString(autoresOferta)
        + super.toString()
        );
  }
}
