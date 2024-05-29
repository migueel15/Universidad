package Supermercado;

import java.util.concurrent.Semaphore;

public class SupermercadoSemaforos implements Supermercado {

	
	
	private Cajero permanente;

	
	
	public SupermercadoSemaforos() throws InterruptedException {
		permanente = new Cajero(this, true); //crea el primer cajero, el permanente
		permanente.start();		
		//TODO
	}

	@Override
	public void fin() throws InterruptedException {
		
	}

	@Override
	public void nuevoCliente(int id) throws InterruptedException {
		
	}

	@Override
	public boolean permanenteAtiendeCliente( int id) throws InterruptedException {
		
			
		return true;//borrar
		
	}
		
	
	@Override
	public boolean ocasionalAtiendeCliente(int id) throws InterruptedException {
			
	
		return true;//borrar
	}

}
