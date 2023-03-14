package concesionario;

public class Coche {
  private static final double GASTOS_MATR = 100;
  private static double iva = 10.0;
  private static int refCnt = 1;
  private final int referencia;
  private final String modelo;
  private double precio;

  public Coche(String modelo) {
    this(modelo, 0.0);
  }

  public Coche(String modelo, double precio) {
    this.modelo = modelo;
    this.precio = precio;
    referencia = refCnt;
    refCnt++;
  }

  public double calcPrecioFinal() {
    return (GASTOS_MATR + precio) * (1 + iva / 100);
  }

  public int getRef() {
    return referencia;
  }

  @Override
  public String toString() {
    return "(" + referencia
        + ", " + modelo + ", "
        + calcPrecioFinal() + ")";
  }

  public String getModelo() {
    return modelo;
  }

  protected double getPrecioBase() {
    return precio;
  }

  public void setPrecioBase(double valor) {
    precio = valor;
  }

  public static double getIva() {
    return iva;
  }

  public static void setIva(double newIva) {
    iva = newIva;
  }
}
