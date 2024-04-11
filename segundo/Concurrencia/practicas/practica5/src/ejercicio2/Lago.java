package ejercicio2;

//Recurso compartido
public class Lago {
	private int nivel;
	
	public Lago(){
		nivel = 0;
	}
	
	public int get(){
		return nivel;
	}
	
	//La capacidad del lago se supone indefinida
	public void incrementa(int id, int iter){ 
		nivel++;
		System.out.println(iter+":Rio " + id + " ha incrementado el nivel: "+nivel);
		
	}
	
	
	public void decrementa(int id, int iter){ 
		nivel--;
		System.out.println(iter+":Presa " + id + " ha decrementado el nivel: " +nivel);
	}
}
