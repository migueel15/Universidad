import java.util.ArrayList;
import java.util.List;

public class LaberintoSumergido {
  private int[][] laberinto;
  private Posicion entrada, salida;
  private int n;
  private int cantidadAire; // Cantidad de aire que tiene el buzo.

  public LaberintoSumergido(int[][] lab, Posicion ent, Posicion sal, int c) {
    this.laberinto = lab;
    this.n = laberinto.length;
    this.entrada = ent;
    this.salida = sal;
    cantidadAire = c;
  }

  public int getNumFilas() {
    return n;
  }

  public int getNumCols() {
    return n;
  }

  public int[][] getLaberinto() {
    return laberinto;
  }

  public Posicion getEntrada() {
    return entrada;
  }

  public Posicion getSalida() {
    return salida;
  }

  public int cantidadAire() {
    return cantidadAire;
  }

  /**
   * Dada una posición cartesiana devuelve la siguiente posición en el sentido
   * indicado. Precondición: actual != null
   *
   * @param actual Posición de partida
   * @param dir    Sentido en el que hay que desplazarse (1->Norte, 2->Sur,
   *               3->Este, 4-> Oeste)
   * @return La nueva posición.
   */
  private Posicion siguiente(Posicion actual, int dir) {
    int x = actual.getX();
    int y = actual.getY();
    if (dir == 1) {
      x--;
    } else if (dir == 2) {
      x++;
    } else if (dir == 3) {
      y++;
    } else {
      y--;
    }
    return new Posicion(x, y);
  }

  private boolean estaEnLaberinto(Posicion pos) {
    return pos.getX() >= 0 && pos.getX() < getNumFilas() && pos.getY() >= 0 && pos.getY() < getNumCols();
  }

  private boolean esMuro(Posicion p) {
    if (estaEnLaberinto(p)) {
      return laberinto[p.getX()][p.getY()] == -1;
    } else
      return false;
  }

  private boolean quedaOxigeno(Posicion p) {
    return laberinto[p.getX()][p.getY()] < cantidadAire;
  }

  private boolean valida(Posicion candidata, List<Posicion> sol) {
    return !sol.contains(candidata) && !esMuro(candidata) && estaEnLaberinto(candidata) && quedaOxigeno(candidata);
  }

  private boolean esCompleta(List<Posicion> sol) {
    // si tiene que pasar por una posicion sol tiene que contener esa pos.
    return sol.contains(entrada) && sol.contains(salida) && cantidadAire > 0;
  }

  public List<List<Posicion>> encontrarCaminos() {
    List<List<Posicion>> todosCaminos = new ArrayList<List<Posicion>>();
    List<Posicion> sol = new ArrayList<Posicion>();
    sol.add(entrada);
    encontrarCaminos(sol, todosCaminos, cantidadAire);
    return todosCaminos;
  }

  /**
   * Algoritmo de Vuelta Atrás para encontrar todas las soluciones
   */
  private void encontrarCaminos(List<Posicion> sol,
      List<List<Posicion>> todas, int oxigeno) {
    if (esCompleta(sol)) {
      todas.add(sol);
    } else {
      for (int i = 1; i <= 4; i++) {
        if (valida(siguiente(sol.get(sol.size() - 1), i), sol)) {
          ArrayList<Posicion> aux = new ArrayList<>(sol);
          Posicion pos = siguiente(sol.get(sol.size() - 1), i);
          int auxOxigeno = oxigeno - laberinto[pos.getX()][pos.getY()];
          aux.add(pos);
          encontrarCaminos(aux, todas, auxOxigeno);
        }
      }
    }
  }

  private int calidad(List<Posicion> sol) {
    // ***Completar la implementación****
    if (sol == null) {
      return Integer.MAX_VALUE;
    } else
      return sol.size();
  }

  public List<Posicion> encontrarCaminoOptimo() {
    List<Posicion> sol = new ArrayList<Posicion>();
    sol.add(entrada);
    return encontrarCaminoOptimo(sol, null);
  }

  private List<Posicion> encontrarCaminoOptimo(List<Posicion> sol, List<Posicion> mejor) {
    List<List<Posicion>> resultados = encontrarCaminos();
    for (List<Posicion> l : resultados) {
      if (calidad(l) < calidad(mejor)) {
        mejor = l;
      }
    }
    return mejor;
  }
}
