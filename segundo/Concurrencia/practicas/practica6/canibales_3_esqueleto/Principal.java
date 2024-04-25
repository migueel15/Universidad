package canibales_3_esqueleto;

public class Principal {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int NumCan = 10;
		//IOlla olla = new Olla1();
		IOlla olla = new Olla();
		
		Cocinero cocinero = new Cocinero(olla);
		
		Canibal[] canibal = new Canibal[NumCan];
		
		for (int i = 0; i<canibal.length; i++)
			canibal[i] = new Canibal(olla,i);
		
		cocinero.start();
		for (int i = 0; i<canibal.length; i++)
			canibal[i].start();
	}

}
