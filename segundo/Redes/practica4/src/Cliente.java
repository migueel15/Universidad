import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class Cliente {

	public static void main(String[] args) {
    String cadena = "Hola mundo";
    DatagramSocket s = null;
    try {
      s = new DatagramSocket();
    } catch (SocketException e) {
      System.out.println("No se ha podido crear el Socket");
    }

    DatagramPacket dp = null;
    try {
      dp = new DatagramPacket(cadena.getBytes(StandardCharsets.UTF_8), cadena.length(),
          InetAddress.getByName("localhost"),
          2222);
    } catch (UnknownHostException e) {
      System.out.println("Error al crear el paquete");
    }
    try {
      if (s != null) {
        s.send(dp);
      }
    } catch (IOException e) {
      System.out.println("Error al enviar el paquete");
    }
    s.close();
  }

}