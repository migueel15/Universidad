package Supermercado;

public class SupermercadoMonitores implements Supermercado{

	private Cajero permanente;

	
	
	public SupermercadoMonitores() throws InterruptedException {
		permanente = new Cajero(this, true); //crea el primer cajero, el permanente
		permanente.start();
		
		//TODO
	}
	
	@Override
	public void fin() throws InterruptedException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void nuevoCliente(int id) throws InterruptedException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean permanenteAtiendeCliente(int id) throws InterruptedException {
		// TODO Auto-generated method stub
		return false;//borrar
	}

	@Override
	public boolean ocasionalAtiendeCliente(int id) throws InterruptedException {
		// TODO Auto-generated method stub
		return false;//borrar
	}

}
