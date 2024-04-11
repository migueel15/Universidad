package ejercicio1;

public class Main {
  public static void main(String[] args) throws InterruptedException {
    int ITERACIONES = 10000;

    Printer p1 = new Printer('o',ITERACIONES);
    Printer p2 = new Printer('l',ITERACIONES);
    Printer p3 = new Printer('a',ITERACIONES);

    // Si se mezclan. Las tres hebras se ejecutan simultaneamente sin ningún
    // control sobre el orden que deben seguir.
    p1.start();
    p2.start();
    p3.start();

    p1.join();
    p2.join();
    p3.join();
  }
}
