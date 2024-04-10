package _0ComunicacionHebras;

public class RecursoCompartido {
  private int var;

  /*
   * public Variable(T var){
   * this.var = var;
   * }
   */

  public void almacena(int dato) {
    var = dato;
  }

  public int extrae() {
    return var;
  }
}
