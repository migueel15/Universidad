package sensores_2_esqueleto;

public class Principal {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Mediciones m = new Mediciones();
		Sensor[] s = new Sensor[3];
		
		for (int i = 0; i<s.length; i++)
			s[i] = new Sensor(m,i);
		
		Trabajador w = new Trabajador(m);
		
		for (int i = 0; i<s.length; i++)
			s[i].start();
		w.start();
	}

}
