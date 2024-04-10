package _0ComunicacionHebras;

public class ProdCons {
  public static void main(String[] args) {
    try {
      // Variable<Integer> v = new Variable<Integer>(0);
      RecursoCompartido v = new RecursoCompartido();
      Productor p = new Productor(10, v);
      Consumidor c = new Consumidor(10, v);

      p.start();
      c.start();

      p.join();
      c.join();

    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
