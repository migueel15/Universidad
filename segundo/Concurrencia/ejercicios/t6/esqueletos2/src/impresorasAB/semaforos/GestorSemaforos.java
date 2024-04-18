package impresorasAB.semaforos;

import java.util.concurrent.Semaphore;

/**
 * 
 * @author monica
 * Soluci�n al problema del gestor de impresoras utilizando
 * condiciones. La condici�n colaGeneral es utilizada por todas las
 * hebras cuando piden una impresora y no hay del tipo que piden.
 *
 * Las colas colaA y colaB son utilizadas en exclusiva por las hebras de tipo
 * A y B, respectivamente.
 * 
 * 
 * 
 */
public class GestorSemaforos implements Gestor {

	private int numImpA,numImpB; //numero de impresoras de cada tipo

	public GestorSemaforos(int numA,int numB){
		numImpA = numA;
		numImpB = numB;
	}

	public void qImpresoraA(int id) throws InterruptedException{
		//Cliente A solicita impresora
	}


	public void qImpresoraB(int id) throws InterruptedException{
		//Cliente B solicita impresora
	}


	public char qImpresoraAB(int id) throws InterruptedException{
		//Cliente AB solicita impresora
		char valor='A';
		
		return valor;
	}


	public void dImpresora(char tipo) throws InterruptedException{
		//Cliente deja una impresora, indicando el tipo de la impresora
		
	}
}
