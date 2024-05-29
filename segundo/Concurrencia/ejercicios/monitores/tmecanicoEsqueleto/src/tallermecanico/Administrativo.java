package tallermecanico;

public class Administrativo extends Thread {

	private Taller taller;

	public Administrativo(Taller taller) {
		this.taller = taller;
	}

	public void run() {
		while (true) {
			try {
				taller.esperaParaFacturar();
				Thread.sleep(500);
				taller.finFactura();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
