import jarras.Mesa;

public class EjemploUsoMesa1 {
  public static void main(String[] args) {
    Mesa mesa = new Mesa(7,5);
    mesa.llena(2);
    System.out.println(mesa);
    mesa.llenaDesde(2);
    System.out.println(mesa);
    mesa.llena(2);
    System.out.println(mesa);
    mesa.llenaDesde(2);
    System.out.println(mesa);
    mesa.vacia(1);
    System.out.println(mesa);
    mesa.llenaDesde(2);
    System.out.println(mesa);
    mesa.llena(2);
    System.out.println(mesa);
    mesa.llenaDesde(2);
    System.out.println(mesa);
  }
}
