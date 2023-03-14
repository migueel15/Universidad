package mvc;

public class Modelo {
  private double total;
  public Modelo() {
    total = 0;
  }
  public double consultarTotal() {
    return total;
  }
  public void sumarValor(double valor) {
    total += valor;
  }
}
