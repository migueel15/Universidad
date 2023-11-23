import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ComparaMetodos {
/*
	public static void main(String[] args) {
		int [] tam = //Selecciona tamaños de entrada
		int numPruebas = //Selecciona número de pruebas por tamaño
		String [] competidores = //Selecciona orden de los algoritmos competidores
		ProcesaLista miMetodo = new ProcesaLista();
		
		for (int c = 0; c < competidores.length; c++) {
			Metodo competidor = FabricaMetodos.crear(Metodo.Orden.valueOf(competidores[c]));
			long[] tiemposC = Complejidad.medirTiemposRobusto(competidor,tam, numPruebas);
			long[] misTiempos = medirTiemposLista(miMetodo, tam, numPruebas);
			
			System.out.println("Tamaños de entrada\tMi Método\t" + competidor.ordenPeorCaso()+"\t\tT1/T2");
			for (int i = 0; i < tam.length; i++) {
					System.out.println(tam[i] + "\t\t\t" + misTiempos[i]+"\t\t" + 
										tiemposC[i] + "\t\t" +(double)misTiempos[i]/(double)tiemposC[i]);
			}
		}
	}
	*/
	private static long[] medirTiemposLista(ProcesaLista m, int[]tam, int pruebasPorTam) {
		long [] tiempos = new long[tam.length];
		
		for (int idxTam=0; idxTam < tam.length; idxTam++) {
			//Medimos el coste para tamaño tam[idxTam]
			tiempos[idxTam] = Long.MAX_VALUE;
			for (int idxPrueba = 0; idxPrueba < pruebasPorTam; idxPrueba++) {
				//Preparamos el contenido de la lista
				m.setLista(crearListaOrdenada(tam[idxTam]));
				//Medimos
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
	
	//Algoritmo que en tiempo O(n) genera una lista ordenada de n elementos.
	private static List<Integer> crearListaOrdenada(int n) {
		List<Integer> l = new ArrayList<>();
		Random r = new Random();
		int numero = 0;
		int i = 0;
		while (i < n) {
			//Repetiremos número entre 0 y 3 veces
			for (int j = 0; j < r.nextInt(4); j++) {
				l.add(numero);
				i++;
			};  
			//Pasamos al siguiente número
			numero++;
		}
		return l;
	}

}
