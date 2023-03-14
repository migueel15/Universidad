package libreria;

import java.util.ArrayList;

public class Libreria {
  private ArrayList<Libro>libs;

  public  Libreria(){
    libs = new ArrayList<>();
  }

  public void addLibro(String autor, String titulo, double precio){
    Libro libro = new Libro(autor, titulo, precio);
    anyadirLibro(libro);
  }

  public void remLibro(String autor, String titulo){
    int posicion = buscarLibro(autor, titulo);
    if( posicion > -1) {
      libs.remove(posicion);
    }else{
      throw new RuntimeException("Libro no encontrado (" + autor + ", "
          + titulo + ")");
    }
  }

  public double getPrecioFinal(String autor, String titulo){
    int posicion = buscarLibro(autor, titulo);
    if( posicion > -1) {
      return libs.get(posicion).getPrecioFinal();
    }else{
      throw new RuntimeException("Libro no encontrado (" + autor + ", "
          + titulo + ")");
    }
  }

  @Override
  public String toString() {
    return libs.toString();
  }

  public void mostrarPrecioFinal(String autor, String titulo){
    System.out.println("PrecioFinal("+autor+", "+titulo+"): " + getPrecioFinal(autor, titulo));
  }

  protected void anyadirLibro(Libro libro){
    int posicion = buscarLibro(libro.getAutor(), libro.getTitulo());
    if ( posicion > -1 ){
      libs.set(posicion, libro);
    }else{
      libs.add(libro);
    }
  }

  private boolean mismoAutor(int pos, String valor){
    return libs.get(pos).getAutor().equalsIgnoreCase(valor);
  }

  private boolean mismoTitulo(int pos, String valor){
    return libs.get(pos).getTitulo().equalsIgnoreCase(valor);
  }

  private int buscarLibro(String autor, String titulo){
    int posicion = 0;
    while (
        posicion < libs.size()
        && (!mismoTitulo(posicion,titulo)
        || !mismoAutor(posicion, autor))
    ){
      posicion++;
    }
    return (posicion < libs.size()) ? posicion : -1;
  }
}
