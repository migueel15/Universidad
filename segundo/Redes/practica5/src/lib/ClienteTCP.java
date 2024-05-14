package lib;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class ClienteTCP {
	String ip_destino;
	int port;
	Socket cliente;

	public ClienteTCP(String ip, int port){
		this.ip_destino = ip;
		this.port = port;
	}

	public void startClient() throws IOException {
		cliente = new Socket(ip_destino,port);
		System.out.println("Cliente conectado a: " + ip_destino + ":" + port);

		BufferedReader in =
				new BufferedReader(new InputStreamReader(cliente.getInputStream(),
						StandardCharsets.UTF_8));
		PrintWriter out = new PrintWriter(cliente.getOutputStream(),true, StandardCharsets.UTF_8);

		Scanner entrada = new Scanner(System.in);
		String linea = "";
		while(!linea.equals("FINISH")){
			linea = entrada.nextLine();
			out.println(linea);

			String respuesta = in.readLine();
			System.out.println(respuesta);
		}
		entrada.close();



		cliente.close();
		in.close();
		out.close();
	}
}
