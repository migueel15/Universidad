package sensores_2_esqueleto;

import java.util.concurrent.*;

public class Mediciones {
    private int mediciones [];
    private int numMediciones = 0;
	
	public Mediciones(){
		mediciones = new int[3];

	}
	
	public void nuevaMedicion(int id, int valor){
	
	}
	
	public int[] leerMediciones() throws InterruptedException{
		//El trabajador se debe quedar bloqueado mientras no haya mediciones

		System.out.println("El trabajador lee las mediciones");
		
		return mediciones;
	}
	
	public void finTarea(){
		//El trabajador termina y despierta a los tres sensores
		System.out.println("El trabajador termina su tarea");

	}

}
