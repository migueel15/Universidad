import lib.ServidorTCP;

import java.io.IOException;

public class Servidor {
	public static void main(String[] args) {
		ServidorTCP servidor = new ServidorTCP(20000);
		try {
			servidor.startServer();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
