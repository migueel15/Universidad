import lib.Cliente;

public class ClienteUDP {
	public static void main(String[] args) {
		if(args.length != 2){
			System.out.println("Uso: ClienteUDP <ip_destino> <puerto>");
			return;
		}
		String ip_destino = args[0];
		int port = Integer.parseInt(args[1]);
		Cliente cliente = new Cliente(ip_destino,port);
		cliente.start();
	}
}
