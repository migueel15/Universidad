public abstract class Metodo {
	public enum Orden {CTE, LOGN, N, NLOGN, N2, N3, EXP2};
	
	private Orden peorCaso;
	private Orden mejorCaso;
	
	public Metodo () {
		this(null,null);
	}
	
	public Metodo(Orden mejor, Orden peor) {
		peorCaso = peor;
		mejorCaso = mejor;
	}
	
	public Metodo(Orden unicoCaso) {
		this(unicoCaso,unicoCaso);
	}
	
	public Orden ordenMejorCaso () {
		return mejorCaso;
	}
	
	public Orden ordenPeorCaso() {
		return peorCaso;
	}
	
	// codigo será un método que tendrá la complejidad indicada por mejorCaso y  peorCaso
	public abstract int codigo (int n);
	
	
}
