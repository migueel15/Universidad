public class VehiculoTerrestre extends Vehiculo{
	protected int numRuedas = 0;

	public VehiculoTerrestre(Entorno e, int numRuedas) {
		super(e);
		this.setMaxVelocidad(30.0);
		this.setDelta(5.0);
		this.numRuedas = numRuedas;
	}

	public void setNumRuedas(int numRuedas) {
		this.numRuedas = numRuedas;
	}

	public int getNumRuedas() {
		return numRuedas;
	}

	@Override
	protected void avanzar(double segundos) {
		super.avanzar(segundos);
	}
}
