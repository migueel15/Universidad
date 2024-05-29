package tallermecanico;

public class Cliente extends Thread{
	private Taller taller;
	private int id;
	
	public Cliente(int id, Taller taller){
		this.id = id;
		this.taller = taller;
	}
	
	public void run(){
		while (true){
			try {
				taller.revisarCoche(id);
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
