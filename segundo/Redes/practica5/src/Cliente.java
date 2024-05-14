import lib.ClienteTCP;

import java.io.IOException;

public class Cliente {
	public static void main(String[] args) {
		ClienteTCP cliente = new ClienteTCP("127.0.0.1", 20000);
		try {
			cliente.startClient();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
