package ejercicio2;

public class Peterson {
  private volatile int turno = 0;
  private volatile boolean f0 = false;
  private volatile boolean f1 = false;

  public void protEntradaP0() {
    f0 = true;
    turno = 1;
    while (f1 && (turno == 1)) {
      Thread.yield();
    }
  }
  public void protSalidaP0() {
    f0 = false;
  }

  public void protEntradaP1() {
    f1 = true;
    turno = 0;
    while (f0 && (turno == 0)) {
      Thread.yield();
    }
  }

  public void protSalidaP1() {
    f1 = false;
  }

  public void entrar(int id){
    if (id == 0){
      protEntradaP0();
    }else if (id == 1){
      protEntradaP1();
    }
  }

  public void salir(int id){
    if (id == 0){
      protSalidaP0();
    }else if (id == 1){
      protSalidaP1();
    }
  }
}
