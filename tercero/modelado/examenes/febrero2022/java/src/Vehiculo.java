import java.util.Enumeration;

public abstract class Vehiculo {
	protected double posicion;
	protected double velocidad;
	protected Entorno entorno;
	protected double maxVelocidad;
	protected double delta;

	public Vehiculo(Entorno e){
		this.entorno = e;
		this.posicion = 0;
		this.velocidad = 0;
	}

	public void setMaxVelocidad(double maxVelocidad) {
		this.maxVelocidad = maxVelocidad;
	}

	public double getMaxVelocidad() {
		return maxVelocidad;
	}

	public double getDelta() {
		return delta;
	}

	public void setDelta(double delta) {
		this.delta = delta;
	}

	protected void avanzar(double segundos){}
	protected void aumentaVelocidad(){}
	protected void disminuirVelocidad(){}
}
