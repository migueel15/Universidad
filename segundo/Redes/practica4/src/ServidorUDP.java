import lib.Servidor;

public class ServidorUDP {
	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("Uso: Servidor <puerto>");
			return;
		}
		int port = Integer.parseInt(args[0]);
		Servidor servidor = new Servidor(port);
		servidor.start();
	}
}
