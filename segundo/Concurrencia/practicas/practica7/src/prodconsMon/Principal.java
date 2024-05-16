package prodconsMon;

public class Principal {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		final int NCONS = 3;
		
		Buffer b = new Buffer(NCONS);
		
		Productor prod = new Productor(b);
		
		Consumidor[] cons = new Consumidor[NCONS];
		
		for (int i=0; i<cons.length; i++)
			cons[i] = new Consumidor(b,i);
		
		prod.start();
		
		for (int i=0; i<cons.length; i++)
			cons[i].start();;
	}
}
