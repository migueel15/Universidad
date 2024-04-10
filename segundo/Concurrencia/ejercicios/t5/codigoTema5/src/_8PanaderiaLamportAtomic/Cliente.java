package _8PanaderiaLamportAtomic;

class Cliente extends Thread {
	private int id;
	private Panaderia pan;

	public Cliente(int id, Panaderia pan) {
		this.id = id;
		this.pan = pan;
	}

	public void run() {
		System.out.println("Cliente " + id + ": coge turno");
		pan.cogeTurno(id);
		System.out.println("Cliente " + id + ": espera su turno");
		pan.esperoTurno(id);
		// el cliente id es atendido por el dependiente
		System.out.println("Cliente " + id + ": es atendido");// el cliente id es atendido por el dependiente
		pan.salePanaderia(id);
		// el cliente id sale de la panadería
		System.out.println("Cliente " + id + ": sale de la panaderia");// el cliente id es atendido por el dependiente
	}
}

