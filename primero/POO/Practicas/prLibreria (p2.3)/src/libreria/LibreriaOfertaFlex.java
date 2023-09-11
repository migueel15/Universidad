package libreria;

public class LibreriaOfertaFlex extends Libreria {
  OfertaFlex ofertaFlex;
  public LibreriaOfertaFlex(OfertaFlex ofertaFlex){
    super();
    this.ofertaFlex = ofertaFlex;
  }

  public void setOferta(OfertaFlex ofertaFlex){
    this.ofertaFlex = ofertaFlex;
  }

  public OfertaFlex getOferta(){
    return ofertaFlex;
  }

  @Override
  public void addLibro(String autor, String titulo, double precioBase){
    Libro libro = new Libro(autor, titulo, precioBase);
    if( ofertaFlex.getDescuento(libro) != 0 ){
      libro = new LibroOferta(autor, titulo, precioBase, ofertaFlex.getDescuento(libro));
    }
    anyadirLibro(libro);
  }

  @Override
  public String toString(){
    return ofertaFlex + super.toString();
  }
}
