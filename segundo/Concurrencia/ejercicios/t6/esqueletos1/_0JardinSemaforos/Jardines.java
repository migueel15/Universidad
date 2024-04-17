package _0JardinSemaforos;

public class Jardines {
  private static int VISITANTES = 10000000;

  public static void main(String[] args) {
    Contador visitantes = new Contador(); // Entidad pasiva. Recurso compartido
    Puerta p1 = new Puerta("P1", visitantes, VISITANTES);
    Puerta p2 = new Puerta("P2", visitantes, VISITANTES);

    p1.start(); // Entidad activa
    p2.start(); // Entidad activa

    try {
      p1.join();
      p2.join();
    } catch (InterruptedException e) {
      System.out.println("La hebra ha sido interrumpida");
    }
    System.out.println("\nEl numero de visitantes contabilizado es " + visitantes.valor());
    System.out.println("\nDeberian ser " + (VISITANTES * 2));
    System.out.println("La diferencia es: " + (VISITANTES * 2 - visitantes.valor()));
  }
}
