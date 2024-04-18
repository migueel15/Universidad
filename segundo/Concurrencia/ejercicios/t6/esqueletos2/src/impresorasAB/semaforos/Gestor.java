package impresorasAB.semaforos;

public interface Gestor {
	public void qImpresoraA(int id) throws InterruptedException;
	public void qImpresoraB(int id) throws InterruptedException;
	public char qImpresoraAB(int id) throws InterruptedException;
	public void dImpresora(char tipo) throws InterruptedException;
}
