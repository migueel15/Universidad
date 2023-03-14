package libreria;

public class LibroOferta extends Libro{
  private double porcDescuento;
  public LibroOferta(String autor, String titulo, double precioBase, double descuento){
    super(autor, titulo, precioBase);
    this.porcDescuento = descuento;
  }

  public double getDescuento(){
    return this.porcDescuento;
  }

  @Override
  protected double getBaseImponible(){
    return getPrecioBase() - (getPrecioBase() * getDescuento() / 100);
  }

  @Override
  public String toString() {
    return ("("+getAutor() + "; " + getTitulo()
        + "; " + getPrecioBase() + "; "
        + getDescuento() + "%; "
        + getBaseImponible() + "; "
        + Libro.getIVA() + "%; "
        + getPrecioFinal() + ")");
  }
}
