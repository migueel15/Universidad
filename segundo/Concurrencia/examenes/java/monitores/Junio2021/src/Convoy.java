public class Convoy {
	
	public Convoy(int tam) {
		//TODO
	}
	
	/**
	 * Las furgonetas se unen al convoy
	 * La primera es la lider, el resto son seguidoras 
	 **/
	public int unir(int id){
		//TODO: Poner los mensajes donde corresponda
		System.out.println("** Furgoneta " +id + " lidera del convoy **");
		
		System.out.println("Furgoneta "+id+" seguidora");
		return 0;
	}

	/**
	 * La furgoneta lider espera a que todas las furgonetas se unan al convoy 
	 * Cuando esto ocurre calcula la ruta y se pone en marcha
	 * */
	public void calcularRuta(int id){
		//TODO
		System.out.println("** Furgoneta "+id+" lider:  ruta calculada, nos ponemos en marcha **");
	}
	
	
	/** 
	 * La furgoneta lider avisa al las furgonetas seguidoras que han llegado al destino y deben abandonar el convoy
	 * La furgoneta lider espera a que todas las furgonetas abandonen el convoy
	 **/
	public void destino(int id){
		//TODO
		
		System.out.println("** Furgoneta "+id+" lider abandona el convoy **");
	}

	/**
	 * Las furgonetas seguidoras hasta que la lider avisa de que han llegado al destino
	 * y abandonan el convoy
	 **/
	public void seguirLider(int id){
		//TODO
		System.out.println("Furgoneta "+id+" abandona el convoy");
	}

	
	
	/**
	* Programa principal. No modificar
	**/
	public static void main(String[] args) {
		final int NUM_FURGO = 10;
		Convoy c = new Convoy(NUM_FURGO);
		Furgoneta flota[ ]= new Furgoneta[NUM_FURGO];
		
		for(int i = 0; i < NUM_FURGO; i++)
			flota[i] = new Furgoneta(i,c);
		
		for(int i = 0; i < NUM_FURGO; i++)
			flota[i].start();
	}

}
