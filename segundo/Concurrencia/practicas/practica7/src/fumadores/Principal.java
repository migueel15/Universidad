package fumadores;

public class Principal {
	public static void main(String[] args){
		Mesa m = new Mesa(); //Recurso compartido
		
		Agente agente = new Agente(m);
		
		Fumador[] fumador = new Fumador[3];
		
		for (int i = 0; i < fumador.length; i++)
			fumador[i] = new Fumador(m,i);
		
		agente.start();
		for (int i = 0; i < fumador.length; i++)
			fumador[i].start();
	}

}
