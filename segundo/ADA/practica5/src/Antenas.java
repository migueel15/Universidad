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
	
	
}
