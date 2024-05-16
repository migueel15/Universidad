package prodconsMon;

import java.util.Arrays;

public class Buffer {
	//Buffer
	private int N = 10; //Tamaño del buffer
	private int[] buffer = new int[N]; //Buffer
	private int[] numCons = new int[N];//Contador del numero de consumidores que faltan por leer cada elemento del buffer
	//Numero de espacios que hay en el buffer
	private int nespacios = N;
	private int ncons; //Numero de consumidores del buffer
	private int[] nelems; //Numero de elementos en el buffer para cada consumidor
	private int[] c; //posicion a partir de la que consume cada consumidor
	private int p; //posicion a partir de la que guarda el productor

	//n - numero de consumidores
	public Buffer(int n){
		System.out.println("Tamanio del buffer " + N);
		System.out.println("Numero de consumidores " + n + "\n");

	}

	//synchronized (ex. mutua) + wait/notify (cond. sincronizaci�n)
	public void almacenar(int elem) {
		//CS-Productor: espera mientras el buffer est� lleno

		//actualiza todas las variables


	}

	//id- identificador del consumidor
	public int extrae(int id){
		//CS-Consumidor_id: espera mientras no tenga elementos que consumir

		//actualiza todas las variables
		
		return v;
	}
}
