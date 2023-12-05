public class Posicion {
	private int x;
	private int y;

	public Posicion(int i, int j) {
		this.x = i;
		this.y = j;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public boolean equals(Object o) {
		return (o instanceof Posicion) && ((Posicion) o).x == x && ((Posicion) o).y == y;
	}

	public int hashCode() {
		return x * 7 + y * 17;
	}

	public String toString() {
		return "(" + x + "," + y + ")";
	}

}
