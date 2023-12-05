import java.util.List;

public class Estado implements Comparable<Estado>{
	private List<Posicion> camino;
	private int valorCota;

	public Estado(List<Posicion> c, int cota) {
		camino = c;
		valorCota = cota;
	}
	
	public List<Posicion> getCamino() {
		return camino;
	}
	public int calidad() {
		return camino.size();
	}
	
	public int cota() {
		return valorCota;
	}
	@Override
	public int compareTo(Estado o) {
		return this.cota() - o.cota();
	}

}
