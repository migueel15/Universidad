package ejercicio2Peterson_esqueleto;

public class Principal {
	public static void main(String[] args){
		final int MAX = 10;
		Lago lago = new Lago();     //Recurso compartido

		//COMPLETAR
		
		//Mostramos el nivel final del lago
		System.out.println("Nivel Final Lago: "+ lago.get());
	}
}
