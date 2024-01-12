import java.util.List;

public class MainAntena {

  public static void main(String[] args) {
    Integer[] p1 = { 13, 25, 35, 43, 45 };
    int c1 = 7;

    Antenas a1 = new Antenas(p1, c1);
    List<Integer> s1 = a1.situarAntenas();
    mostrarSolucion(s1);

  }

  private static void mostrarSolucion(List<Integer> sol) {
    if (sol != null) {
      System.out.println("La solución obtenida sitúa " + sol.size() + " antenas. En las siguientes posiciones: ");
      for (int i = 0; i < sol.size(); i++) {
        System.out.print(sol.get(i) + " ");
      }
    } else {
      System.out.println("Este problema no tiene solución, considera aumentar el número de antenas.");
    }

  }

}
