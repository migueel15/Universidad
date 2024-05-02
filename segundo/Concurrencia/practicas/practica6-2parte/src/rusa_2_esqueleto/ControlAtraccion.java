package rusa_2_esqueleto;

public class ControlAtraccion extends Thread{
	
	private Coche coche;
	
	public ControlAtraccion (Coche c){
		this.coche = c;
	}
	
	public void run(){
		boolean fin = false;
		while (!this.isInterrupted() && !fin){
			try{
				coche.inicioViaje();	
				System.out.println("Atraccion funcionando");
				Thread.sleep(200);
				coche.finViaje();
			}catch (InterruptedException ie){
				fin = true;
			}	
		}
	}

}
