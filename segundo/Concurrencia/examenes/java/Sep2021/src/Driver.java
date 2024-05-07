public class Driver {

	public static void main(String[] args) {
		final int NPROCESADORES=10;
		final int NDATOS = 3;
		
		DatoCompartido dato = new DatoCompartido(NPROCESADORES);
		Generador e = new Generador(NDATOS,dato);
		e.start();
		
		Procesador [] l= new Procesador[NPROCESADORES];
		for (int i=0; i<NPROCESADORES; i++) {
			l[i] = new Procesador(i,NDATOS,dato);
			l[i].start();
		}
		
	}

}
