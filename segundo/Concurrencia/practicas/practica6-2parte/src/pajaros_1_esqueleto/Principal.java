package pajaros_1_esqueleto;

public class Principal {

	public static void main(String[] args) {
		Nido nido = new Nido(7);
		Pajaro p0 = new Pajaro(0, nido);
		Pajaro p1 = new Pajaro(1, nido);
		Polluelo polluelos[] = new Polluelo[5];
		for (int i = 0; i < 5; i++)
			polluelos[i] = new Polluelo(i, nido);
		p0.start();
		p1.start();
		for (int i = 0; i < 5; i++)
			polluelos[i].start();

		try {
			Thread.sleep(2000);
			p0.interrupt();
			p1.interrupt();
			for (int i = 0; i < 5; i++)
				polluelos[i].interrupt();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
