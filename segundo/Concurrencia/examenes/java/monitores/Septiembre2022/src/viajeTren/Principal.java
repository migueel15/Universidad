package viajeTren;

public class Principal {

	public static void main(String[] args) {
		Tren tren = new Tren();
		Maquinista m = new Maquinista(tren);
		Pasajero[] pas = new Pasajero[20];
		for (int i=0; i<pas.length; i++)
			pas[i] = new Pasajero(tren,i);
		m.start();
		for (int i=0; i<pas.length; i++)
			pas[i].start();
	}
}
