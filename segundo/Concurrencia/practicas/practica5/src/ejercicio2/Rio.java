package ejercicio2;

public class Rio extends Thread{
	Lago lago;
	private static int MAX_RIO;
	private int id;

	public Rio(Lago lago, int id){
		this.lago = lago;
		this.id = id;
	}

	public void run(){
		for(int i = 0;  i < 1000; i++){
			lago.incrementa(id, i);
		}
	}
}
