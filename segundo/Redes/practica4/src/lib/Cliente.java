package lib;

import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Cliente {
	private int puerto;
	private String ip_destino;
	private DatagramPacket dp = null;
	private DatagramSocket ds = null;

	public Cliente(String ip_destino, int puerto) {
		this.ip_destino = ip_destino;
		this.puerto = puerto;
		try {
			ds = new DatagramSocket();
			System.out.println("Conectado a " + this.ip_destino + ":" + this.puerto);
		} catch (SocketException e) {
			System.out.println("No se ha podido crear el Socket");
		}
	}

	public void sendData(String data) {
		try {
			dp = new DatagramPacket(data.getBytes(StandardCharsets.UTF_8),
					data.length(),
					InetAddress.getByName(this.ip_destino),
					this.puerto);
		} catch (UnknownHostException e) {
			System.out.println("Error al crear el paquete");
		}

		try {
			ds.send(dp);
			System.out.println("Esperando respuesta...");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void recieveData() {
		byte[] buffer = new byte[1000];
		dp = new DatagramPacket(buffer, buffer.length);
		try {
			ds.receive(dp);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		String recibido = new String(dp.getData(), dp.getOffset(), dp.getLength(), StandardCharsets.UTF_8);
		System.out.println(recibido);
	}

	public void closeConnection() {
		ds.close();
	}

	public void start() {
		String mensaje = "";
		Scanner sc = new Scanner(System.in);
		do {
			System.out.println("Escribe un mensaje: ");
			mensaje = sc.nextLine();
			sendData(mensaje);
			recieveData();
		} while (Character.isDigit(mensaje.charAt(0)));
		closeConnection();
	}
}
