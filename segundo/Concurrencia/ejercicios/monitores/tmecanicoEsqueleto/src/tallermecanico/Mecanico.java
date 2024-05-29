package tallermecanico;

public class Mecanico extends Thread{
	private Taller taller;
	
	public Mecanico(Taller taller){
		this.taller = taller;
	}
	
	public void run(){
		while (true){
			try {
				taller.esperaParaRevisar();
				Thread.sleep(500);
				taller.finRevision();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
