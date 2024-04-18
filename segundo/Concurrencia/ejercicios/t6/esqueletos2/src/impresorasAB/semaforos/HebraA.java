package impresorasAB.semaforos;

public class HebraA extends Thread{
	
	private int id;
	private Gestor gestor;
	public HebraA(int id,Gestor gestor){
		this.id = id;
		this.gestor = gestor;
	}
	
	public void run(){
		try{
			gestor.qImpresoraA(id);
			Thread.sleep(100);
			gestor.dImpresora('A');
		} catch (InterruptedException ie){
			
		}
	}

}
