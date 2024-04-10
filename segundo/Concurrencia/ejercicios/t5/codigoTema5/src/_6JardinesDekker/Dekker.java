package _6JardinesDekker;

public class Dekker {

	private volatile int turno = 0;
	private volatile boolean f0 = false;
	private volatile boolean f1 = false;

	public void preProt0() {
		f0 = true;
		while (f1) {
			if (turno == 1) {
				f0 = false;
				while (turno == 1)
					Thread.yield();
				f0 = true;
			}
		}
	}

	public void postProt0() {
		turno = 1;
		f0 = false;
	}

	public void preProt1() {
		f1 = true;
		while (f0) {
			if (turno == 0) {
				f1 = false;
				while (turno == 0)
					Thread.yield();
				f1 = true;
			}
		}
	}

	public void postProt1() {
		turno = 0;
		f1 = false;
	}
	
	//Métodos adicionales
	public void entrar(int id){
		if (id == 1){
			preProt0();
		}else if (id == 2){
			preProt1();
		}
	}
	
	public void salir(int id){
		if (id == 1){
			postProt0();
		}else if (id == 2){
			postProt1();
		}
	}
}

    
    