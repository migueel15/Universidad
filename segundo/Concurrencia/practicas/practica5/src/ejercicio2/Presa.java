package ejercicio2;

public class Presa extends Thread{
	private static int MAX_PRESA;
	Lago lago;

	public Presa(Lago lago){
		this.lago = lago;
	}

	public void run(){
		for(int i = 0; i < 1000; i++){
			lago.decrementa(i, i);
		}
	}
}
