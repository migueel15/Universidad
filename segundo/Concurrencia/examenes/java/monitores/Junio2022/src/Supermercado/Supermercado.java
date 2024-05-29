package Supermercado;

public interface Supermercado {

	void fin() throws InterruptedException;
	void nuevoCliente(int id) throws InterruptedException;		
	boolean permanenteAtiendeCliente(int id) throws InterruptedException;
	boolean ocasionalAtiendeCliente(int id) throws InterruptedException;

}
