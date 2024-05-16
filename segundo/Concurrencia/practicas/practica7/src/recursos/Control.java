package recursos;

import java.util.*;
public class Control {
	private int rec;//numero total de recursos
	
	private List<Integer> listaEspera = new LinkedList<Integer>(); //Lista de espera para asegurar que las hebras acceden a los recursos en un orden determinado
	
	public Control(int num){
		this.rec = num;
	}
	
	//Misma idea del escritor en el lector/escritor justo
	//Si un proceso quiere un recurso se indica que hay un proceso esperando
	public synchronized void qRecursos(int id,int num) throws InterruptedException {
		listaEspera.add(id);

		while(!listaEspera.get(0).equals(id) || (rec < num)){
			wait();
		}
		rec -= num;
		listaEspera.remove(0);
		System.out.println("Proceso "+id+" quiere "+num+" recursos");
		if(!listaEspera.isEmpty() && rec > 0){
			notifyAll();
		}
	}

	public synchronized void libRecursos(int id,int num) {
		rec += num;
		System.out.println("Proceso "+id+" devuelve "+num+" recursos. Quedan "+rec);
		if(!listaEspera.isEmpty()){
			notifyAll();
		}
	}
}
