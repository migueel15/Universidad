import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainAntena {

	public static void main(String[] args) {
		Integer[] p1 = {5, 9, 23, 35, 44};
		List<Integer> elegidas = new ArrayList<>(Arrays.asList(1,2));
		int c1 = 4;

		Antenas a1 = new Antenas(p1, c1);
		List<Integer> s1 = a1.situarAntenas(elegidas);
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
