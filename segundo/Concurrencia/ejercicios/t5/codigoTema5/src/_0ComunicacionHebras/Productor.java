package _0ComunicacionHebras;

public class Productor extends Thread {
  private static java.util.Random r = new java.util.Random();
  private int numIter;
  private RecursoCompartido var;

  public Productor(int numIter, RecursoCompartido var) {
    this.numIter = numIter;
    this.var = var;
  }

  public void run() {
    int nDato = 0;
    for (int i = 0; i < numIter; i++) {
      nDato = r.nextInt(100);
      System.out.println("Productor " + nDato);
      // Acceso al recurso compartido
      var.almacena(nDato);
    }
  }
}
