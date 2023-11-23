public class Complejidad {
	/**
	 * Devuelve la serie de tiempos estimados tras ejecutar el código del método m, para los tamaños de
	 * entrada dados en tam.
	 */
	public static long[] medirTiempos(Metodo m, int [] tam) {
		long [] tiempos = new long[tam.length];
				
		for (int idxTam=0; idxTam < tam.length; idxTam++) {
			//Medimos el coste para tamaño tam[idxTam]
			long tInicio = System.nanoTime();
			m.codigo(tam[idxTam]);
			tiempos[idxTam] = System.nanoTime()-tInicio;			
		}
		return tiempos;
	}
	
	/**
	 * Devuelve la serie de tiempos estimados tras ejecutar el código del método m, para los tamaños de
	 * entrada dados en tam.
	 * Para cada tamaño de entrada diferente se medirá el tiempo tantas veces como indique pruebasPorTam y 
	 * se seleccionará el mínimo valor.
	 */
	public static long[] medirTiemposRobusto(Metodo m, int [] tam, int pruebasPorTam) {
		long [] tiempos = new long[tam.length];
					
		for (int idxTam=0; idxTam < tam.length; idxTam++) {
			tiempos[idxTam] = Long.MAX_VALUE;
			for (int idxPrueba = 0; idxPrueba < pruebasPorTam; idxPrueba++) {
				//Medimos el coste para tamaño tam[idxTam]
				long tInicio = System.nanoTime();
				m.codigo(tam[idxTam]);
				long tPasado = System.nanoTime()-tInicio;
				//Nos quedamos con el menor tiempo de las pruebas.
				if (tPasado < tiempos[idxTam]) {
					tiempos[idxTam] = tPasado;
				}
			}
		}
		return tiempos;
	}
	
	
}
