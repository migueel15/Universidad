public class Procesador extends Thread{
	int id, nDatos;
	DatoCompartido dato;
	
	public Procesador(int id, int nDatos, DatoCompartido dato) {
		this.id = id;
		this.nDatos = nDatos;
		this.dato = dato;
	}
	
	public void run() {
		try {
			int d;
			for (int i=0; i<nDatos; i++) {
				//Protocolo de entrada: lee el dato del recurso compartido
				d = dato.leeDato(id);
				//Procesa el dato incrementandolo en 1
				d = d+1;
				//Protocolo de salida: actualiza el dato en el recurso compartido
				dato.actualizaDato(id, d);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
