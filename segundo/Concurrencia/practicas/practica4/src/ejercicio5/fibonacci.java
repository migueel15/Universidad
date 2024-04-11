package ejercicio5;

import java.util.Scanner;

public class fibonacci {
	
	public static void main(String[] args) {
		//Leemos el valor de n de la entrada estándar
		try (Scanner sc = new Scanner(System.in);) {
			System.out.println("Introduce el valor de n: ");
			int n = sc.nextInt();
			
			//n debe ser mayor o igual a 0. En otro caso ERROR
			if (n < 0) {
				System.out.println("Error. Número negativo");
			} else {
				//Paso 1. Creamos una instancia de Fib para calcular Fib(n)
				Fib f = new Fib(n);
				
				//Paso 2. Iniciamos la ejecución de la hebra
				Thread t = new Thread(f);
				t.start();

				//Paso 3. Esperamos a que termina la ejecución de la hebra
				t.join();

				/* Paso 4. Una vez que termina la ejecución consultamos el
				 * valor del Fib(n) invocando el método get_fn() de la hebra
				 */
				System.out.println("Fibonacci("+n+"): "+f.get_fn());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}

//Implementamos una clase que implementa la interfaz Runnable
class Fib implements Runnable {
	//Paso 1. definimos los atributos: n, fn y fn1
	int n, fn, fn1;
	
	
	//Paso 2. definimos el constructor, donde fn y fn1 lo inicializamos a 0
	public Fib(int n) {
		this.n = n;
		fn = 0;
		fn1 = 0;
	}
	
	//Paso 3. Métodos para obtener fn y fn1
	public int get_fn() {
		return fn;
	}
	
	public int get_fn1() {
		return fn1;
	}
	
	/* Paso 4. Definimos el comportamiento de la hebra
	 * 
	 * La ejecución de este método implica calcular los valores de fn y fn1
	 * para que luego se puedan consultar al terminar la ejecución de la hebra
	 * utilizando los métodos get_fn() y get_fn1()
	 */
	
	public void run() {
		try {
			
			if (n == 0) {//Paso 4.1 Si n es 0 actualizamos solo el valor de fn
				fn = 0;
			} else if (n == 1) {//Paso 4.2 Si n es 1 actualizamos valor de fn y fn1
				fn = 1;
				fn1 = 0;
			} else  {
				//Paso 4.3 Creamos la hebra para calcular Fib(n-1)
				Fib fibanterior = new Fib(n-1);
				
				//Paso 4.4. Iniciamos la ejecución de la hebra
				Thread t = new Thread(fibanterior);
				t.start();

				//Paso 4.5. Esperamos a que finalice la ejecución de la hebra
				t.join();

				//Paso 4.6 Actualizamos el valor de fn1 y fn
				fn1 = fibanterior.get_fn();
				fn = fibanterior.get_fn() + fibanterior.get_fn1();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}