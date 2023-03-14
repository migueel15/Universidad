import concesionario.Coche;

public class Main {
    public static void main(String[] args) {
      Coche c1 = new Coche("Peugeot", 4500);
      Coche c2 = new Coche("Renault", 23000);
      System.out.println("IVA: " + Coche.getIva());
      System.out.println(c1);
      System.out.println(c2);
      Coche.setIva(21);
      c1.setPrecioBase(9400);
      System.out.println("IVA: " + Coche.getIva());
      System.out.println(c1);
      System.out.println(c2);
    }
}
