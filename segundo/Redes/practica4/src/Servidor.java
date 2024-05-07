import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.charset.Charset;

public class Servidor {
  public static void main(String[] args) {
    DatagramSocket s;
    try {
      s = new DatagramSocket(2222);
    } catch (SocketException e) {
      System.out.println("Error al crear el socket");
      return;
    }
    byte[] buffer = new byte[1000];
    DatagramPacket dp = new DatagramPacket(buffer,buffer.length);

    while(true){
      try {
        s.receive(dp);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
      String recibido = new String(dp.getData(),dp.getOffset(),dp.getLength()
          , Charset.forName("UTF-8"));
      System.out.println(recibido);
      System.out.println(s.getLocalPort());
    }
  }
}
