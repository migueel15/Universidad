package libreria;

public class Libro {
  private static double porcIVA = 10.0;
  private String autor;
  private String titulo;
  private double precioBase;

  public Libro(String autor, String titulo, double precioBase){
    this.autor = autor;
    this.titulo = titulo;
    this.precioBase = precioBase;
  }

  public String getAutor() {
    return autor;
  }

  public String getTitulo() {
    return titulo;
  }

  public double getPrecioBase() {
    return precioBase;
  }

  protected double getBaseImponible(){
    return getPrecioBase();
  }

  public double getPrecioFinal(){
    double base = getBaseImponible();
    return base + (base*porcIVA/100);
  }

  @Override
  public String toString() {
    return "("+ getAutor() + "; " + getTitulo() + "; "
        + getPrecioBase() + "; "+ getIVA() + "%; "
        + getPrecioFinal() + ")\n";
  }

  public static double getIVA(){
    return porcIVA;
  }

  public static void setIVA(double iva){
    porcIVA = iva;
  }
}
