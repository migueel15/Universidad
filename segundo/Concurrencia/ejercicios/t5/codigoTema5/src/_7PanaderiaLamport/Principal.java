package _7PanaderiaLamport;

public class Principal {
	public static void main(String[] args) {
		int N = 15;
		Panaderia pan = new Panaderia(N);
		Cliente[] c = new Cliente[N];

		for (int i = 0; i < N; i++) {
			c[i] = new Cliente(i, pan);
		}
		for (int i = 0; i < N; i++) {
			c[i].start();
		}
	}
}
