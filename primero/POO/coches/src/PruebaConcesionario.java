import concesionario.*;
public class PruebaConcesionario {
  public static void main(String[] args) {
    Coche c1 = new Coche("modelo1",10000);
    Coche c2 = new Coche("modelo2",15000);
    Coche c3 = new Coche("modelo3",12000);
    Coche c4 = new Coche("modelo4",9000);
    Coche c5 = new Coche("modelo5",13500);
    Coche c6 = new Coche("modelo3",6500);
    CocheColor cc7 = new CocheColor("modelColor",20000,"rojo");

    Concesionario concesionario = new Concesionario();
    concesionario.anyadir(c1);
    concesionario.anyadir(c2);
    concesionario.anyadir(c3);
    concesionario.anyadir(c4);
    concesionario.anyadir(c5);
    concesionario.anyadir(c6);
    concesionario.anyadir(cc7);
    System.out.println(cc7);
    System.out.println(concesionario);
    System.out.println(concesionario.seleccionarPrecio(10000,13000));
    System.out.println(concesionario.cocheMasBarato());
  }
}
