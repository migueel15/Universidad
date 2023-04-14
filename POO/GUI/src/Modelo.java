public class Modelo {
  private double total;
  public Modelo(){
    this.total = 0;
  }

  public double getTotal(){
    return total;
  }

  public void incTotal(double valor){
    total += valor;
  }
}
