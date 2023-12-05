import java.util.ArrayList;
import java.util.List;

public class Antenas {
	
	private Integer[] puntosKm; //Puntos kilometricos de las urbanizaciones ordenados crecientemente.
	private int cobertura;
		
	public Antenas(Integer[] urbanizaciones, int c) {
		puntosKm = urbanizaciones;
		cobertura=c;
		
	}
	
	public List<Integer> situarAntenas(){
		ArrayList<Integer> resultado = new ArrayList<>();

		int pos = puntosKm[0] + cobertura;
		resultado.add(pos);

		for(int i = 1; i < puntosKm.length; i++){
			if(pos + cobertura < puntosKm[i]) {
				pos = puntosKm[i] + cobertura;
				resultado.add(pos);
			}
		}
		return resultado;
	}

	public List<Integer> situarAntenas(List<Integer> elegidas){
		ArrayList<Integer> resultado = new ArrayList<>();

		int posActual = Integer.MIN_VALUE;


		for(int i = 0; i < puntosKm.length; i++){

			if (i == 0){
				if(elegidas.contains(0)){
					posActual = puntosKm[0];
					resultado.add(posActual);
				}else{
					posActual = puntosKm[0] + cobertura;
					resultado.add(posActual);
				}
			}else{

				if(elegidas.contains(i) && !resultado.contains(puntosKm[i])){
					posActual = puntosKm[i];
					resultado.add(posActual);
				}else{
					if(posActual + cobertura < puntosKm[i]) {
						posActual = puntosKm[i] + cobertura;
						resultado.add(posActual);
					}
				}
			}
		}
		return resultado;
	}

	
}
