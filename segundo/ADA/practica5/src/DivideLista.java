
import java.util.ArrayList;
import java.util.List;


public class DivideLista {

	// Ordenar lista (quicksort) --------------------------  //
	private static void intercambia(int[] a, int i , int j){
		int aux = a[i];
		a[i] = a[j];
		a[j] = aux;
	}
	private static int partir(int[] a, int inf, int sup){
		int pivote = a[inf];
		int i = inf+1;
		int j = sup;

		do{
			while (i <= j && a[i] <= pivote){i++;}
			while (i <= j && a[j] > pivote){j--;}
			if(i<j){intercambia(a,i,j);}
		}while (i <= j);
		intercambia(a,inf,j);
		return j;
	}
	public static void ordenar(int[] a, int inf, int sup){
		if(inf < sup){
			int p = partir(a, inf, sup);
			ordenar(a, inf, p-1);
			ordenar(a, p+1, sup);
		}
	}
	// ---------------------------------------------------- //

	private static int diferencia(List<Integer>a, List<Integer>b){
		int sum1 = 0;
		int sum2 = 0;

		for (Integer val : a){
			sum1 += val;
		}
		for (Integer val : b){
			sum2 += val;
		}

		return sum1-sum2;
	}


	/**
	 *
	 * @param datos lista de entrada
	 * @param k     longitud de una de las listas solución
	 * @param a   lista solución 1 (salida)
	 * @param b   lista solución 2 (salida)
	 * @return  la diferencia entre las dos listas
	 *
	 */
	public static int resolverVoraz(int []datos, int k, List<Integer> a, List<Integer> b) {
		ordenar(datos,0,datos.length-1); // ordena de menor a mayor

		if(k <= datos.length/2){
			k = datos.length-k;
		}


		for(int i = 0; i < k; i++){
			a.add(datos[(datos.length-1)-i]);
		}

		for(int i = 0; i < datos.length-k; i++) {
			b.add(datos[i]);
		}

		return diferencia(a,b);
	}

	public static void main(String[] args) {
		int[] lista = {3, 3, 5, 7, 2, 12, 4, 17, 18, 6};
		int k = 7;
		ArrayList<Integer> a = new ArrayList<>();
		ArrayList<Integer> b = new ArrayList<>();
		int res = resolverVoraz(lista,k,a,b);
		System.out.println(res);
	}


}
