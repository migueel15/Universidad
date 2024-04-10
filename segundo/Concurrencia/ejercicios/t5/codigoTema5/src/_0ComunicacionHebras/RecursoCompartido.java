package _0ComunicacionHebras;

public class RecursoCompartido {
  private int var;
  private boolean lleno = false;
  /*
   * public Variable(T var){
   * this.var = var;
   * }
   */

  public void almacena(int dato) {
    while(lleno){
      Thread.yield();
    }
    var = dato;
    System.out.println("Productor " + var);
    lleno = true;
  }

  public int extrae() {
    while(!lleno){
      Thread.yield();
    }
    int dato = var;
    System.out.println("Consumidor " + dato);
    lleno = false;
    return dato;
  }
}
