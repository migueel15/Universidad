package lib;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class ServidorTCP {
	int puerto;
	ServerSocket servidor;
	Socket cliente;

	public ServidorTCP(int port){
		puerto = port;
	}

	public void startServer() throws IOException {
		servidor = new ServerSocket(puerto);
		while(true){
			cliente = servidor.accept();
			BufferedReader in =
					new BufferedReader(new InputStreamReader(cliente.getInputStream(), StandardCharsets.UTF_8));
			PrintWriter out = new PrintWriter(cliente.getOutputStream(), true, StandardCharsets.UTF_8);

			boolean cerrado = false;
			while(!cerrado){
				String texto = in.readLine();
				System.out.println("Recibido: " + texto);
				String respuesta = texto;
				if(texto.equals("FINISH")){
					System.out.println("Termino");
					cerrado = true;
					respuesta = "OK";
				}
				out.println("Respuesta: " + respuesta);
			}

			cliente.close();
			in.close();
			out.close();
		}
	}
}
