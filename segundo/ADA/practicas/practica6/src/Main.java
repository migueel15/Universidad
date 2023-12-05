public class Main {
  public static void main(String[] args) {
    int[][] lab = {{0,-1,0,0,-1,-1}, {0,0,0,-1,0,-1}, {-1,0,-1,0,0,0},{0,0,0,
        0,-1,0},{-1,0,-1,0,-1,0},{0,0,0,0,0,0}};
    Posicion entrada = new Posicion(0,0);
    Posicion salida = new Posicion(3,3);


    Laberinto laberinto = new Laberinto(lab,entrada,salida);
    System.out.println(laberinto.encontrarCamino());
    System.out.println(laberinto.encontrarCaminos());
    System.out.println(laberinto.encontrarCaminoMasCortoVA());
    System.out.println(laberinto.encontrarCaminoMasCortoBB());
  }
}
