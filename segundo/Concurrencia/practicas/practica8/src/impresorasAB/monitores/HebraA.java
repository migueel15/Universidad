package impresorasAB.monitores;

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
			Thread.sleep(1000);
		} catch (InterruptedException ie){
			
		}
		gestor.dImpresora('A');
	}

}
