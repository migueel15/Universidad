package ejercicios;

public class KesimoElemento {

	private static void intercambia(int[] a, int i, int j){
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

	public static int buscarKesimo(int[] a, int k) {
		return buscarKesimo(a, k, 0, a.length - 1);
	}

	private static int buscarKesimo(int[] v, int k, int ini, int fin) {
		if(ini == fin){
			return v[ini];
		}

		int val = partir(v, ini, fin);

		if (val == k){
			return v[val];
		}else if(val > k){
			return buscarKesimo(v, k, ini, val-1);
		}else{
			return buscarKesimo(v, k, val+1, fin);
		}
	}


	public static void main(String[] args) {
		int[] lista = {5,2,6,7,9,4}; // 2,4,5,6,7,9
		int el = buscarKesimo(lista,2);
		System.out.println(el);
	}
}
