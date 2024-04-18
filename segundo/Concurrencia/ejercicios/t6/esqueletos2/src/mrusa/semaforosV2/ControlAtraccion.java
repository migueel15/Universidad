package mrusa.semaforosV2;

public class ControlAtraccion extends Thread{
	
	private Coche coche;
	
	public ControlAtraccion (Coche c){
		this.coche = c;
	}
	
	public void run(){
		boolean fin = false;
		while (!this.isInterrupted() && !fin){
			try{
				coche.esperaLleno();	
				Thread.sleep(200);
				coche.finVuelta();
			}catch (InterruptedException ie){
				fin = true;
			}	
		}
	}

}
