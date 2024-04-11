package ejercicio1;
public class Peterson {
  private volatile int turno = 0;
  private volatile boolean f0 = false;
  private volatile boolean f1 = false;

  public void entrada_productor() {
    f0 = true;
    turno = 1;
    while (f1 && (turno == 1)) {
      Thread.yield();
    }
  }
  public void salida_productor() {
    f0 = false;
  }

  public void entrada_consumidor() {
    f1 = true;
    turno = 0;
    while (f0 && (turno == 0)) {
      Thread.yield();
    }
  }

  public void salida_consumidor() {
    f1 = false;
  }
}