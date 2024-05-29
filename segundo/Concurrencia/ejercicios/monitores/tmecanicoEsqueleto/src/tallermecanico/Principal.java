package tallermecanico;

public class Principal {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Taller taller = new Taller();
		
		Mecanico mec = new Mecanico(taller);
		mec.start();
		
		Administrativo adm = new Administrativo(taller);
		adm.start();
		
		Cliente[] cl = new Cliente[15];
		for (int i = 0; i< 15; i++){
			cl[i] = new Cliente(i,taller);
		}
		
		for (int i = 0; i< 15; i++){
			cl[i].start();
		}

	}

}
