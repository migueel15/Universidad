import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class SMTPClient {

  // Socket para las comunicaciones
  static Socket socket = null;
  // Streams para el envío y recepción
  static InputStream in = null;
  static OutputStream out = null;

  // Esta función se conecta al servidor de SMTP
  static void conectar() {
    try {
      // Conecta el socket al servidor de correo (máquina local en puerto 2222)
      socket = new Socket("127.0.0.1", 25);
      // Obtiene los flujos de recepción (in) y de envío (out)
      in = socket.getInputStream();
      out = socket.getOutputStream();
    } catch (IOException e) {
      System.out.println("Error al conectar al servidor: " + e.getMessage());
      System.exit(-1);
    }
  }

  // Esta función cierra el socket
  static void desconectar() {
    try {
      // Cierrz el socket y los flujos asociados
      in.close();
      out.close();
      socket.close();
    } catch (IOException e) {
      System.out.println("Error al cerrar la conexión con el servidor: " +
                         e.getMessage());
      System.exit(-1);
    }
  }

  // Envío de mensajes
  static void enviar(String mensaje) {
    // Mostramos por consola el mensaje a enviar
    System.out.println("C: " + mensaje);
    // TODO: Recordar en añadir en cada mensaje el \r\n
    mensaje = mensaje + "\r\n";
    try {
      // Envia usando con write
      out.write(mensaje.getBytes());
      // Usamos flush para forzar que el envio se haga en este momento
      out.flush();
    } catch (IOException e) {
      System.out.println("Error al enviar: " + e.getMessage());
      System.exit(-1);
    }
  }

  // Recepción de mensajes
  static void recibir() {
    byte[] buffer = new byte[5000];
    try {
      // Recibe el mensaje con read
      in.read(buffer);
    } catch (IOException e) {
      System.out.println("Error al recibir: " + e.getMessage());
      System.exit(-1);
    }
    // Convertimos el mensaje recibido a String ...
    String recv = new String(buffer, 0, buffer.length);
    // ... eliminamos el \r\n final ...
    recv = recv.substring(0, recv.lastIndexOf('\r'));
    // ... y lo mostramos por pantalla
    System.out.println("S: " + recv);
    // TODO: Validar si el código obtenido es correcto (1xx, 2xx, 3xx) o
    // incorrecto (4xx, 5xx)
    int res_code = Integer.parseInt(recv.substring(0, 3));

    if (res_code >= 400) {
      // TODO: Si es incorrecto enviar al servidor un RSET (use el método
      // enviar)
      enviar("RSET");
      // TODO: Envíe luego el comando QUIT
      enviar("QUIT");
      // Desconectamos del servidor
      desconectar();
      System.exit(0);
    }
  }

  public static void main(String[] args) throws IOException {
    // Flujo de lectura de teclado:
    BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

    // Nos conectamos al servidor
    System.out.print("Conectado al servidor...");
    conectar();
    System.out.println("conectado!");

    // ESQUEMA DEL PROTOCOLO SMTP

    // Recibimos el saludo inicial del servidor
    recibir();

    // Enviamos el HELO y recibimos su respuesta
    enviar("HELO servidor!");
    recibir();

    // Leemos el origen:
    String origen = "";
    while (origen.equals("")) {
      System.out.println("Dime el correo del emisor: ");
      origen = stdIn.readLine();
    }
    // TODO: enviar origen del mensaje y su recibir su respuesta
    enviar("MAIL FROM: " + origen);
    recibir();

    // Ahora los destinos
    List<String> destinos = new ArrayList<String>();
    String destino = "";
    while (destinos.size() == 0 || !destino.equals("")) {
      System.out.println(
          "Dime el correo del destino (linea en blanco para acabar): ");
      destino = stdIn.readLine();
      if (!destino.equals("")) {
        destinos.add(destino);
        // TODO: enviar el destino del mensaje y su recibir su respuesta
        enviar("RCPT TO: " + destino);
        recibir();
      }
    }

    // Ahora enviamos el correo: cabeceras + cuerpo
    // TODO: Enviamos el DATA y recibimos la respuesta
    enviar("DATA");
    recibir();
    // Cabeceras:
    // TODO: Enviar la cabecera From: (no hay que recibir respuesta)
    enviar("From: " + origen);
    // TODO: Enviar las cabeceras To: (no hay que recibir respuesta)
    for (String dest : destinos) {
      enviar("To: " + dest);
    }
    // Leemos el asunto:
    String asunto = "";
    while (asunto.equals("")) {
      System.out.println("Dime el asunto del correo: ");
      asunto = stdIn.readLine();
    }
    // TODO: enviar la cabecera Subject: (no hay que recibir respuesta)
    enviar("Subject: " + asunto);

    // Enviamos una línea en blanco para separar las cabeceras del cuerpo
    enviar("");

    // Ahora el cuerpo que son muchas líneas
    String cuerpo = "";
    System.out.println("Dime el mensaje (linea en blanco para acabar): ");
    do {
      cuerpo = stdIn.readLine();
      if (!cuerpo.equals("")) {
        // TODO: enviar la línea (no hay que recibir respuesta)
        enviar(cuerpo);
      }
    } while (!cuerpo.equals(""));
    // TODO: Enviar una línea en blanco y luego un punto para acabar y recibir
    // la respuesta
    enviar("");
    enviar(".");
    recibir();

    // TODO: Enviar QUIT para acabar y recibir la respuesta
    enviar("QUIT");
    recibir();

    // Nos desconectamos del servidor
    System.out.print("Conectado al servidor...");
    desconectar();
    System.out.println("desconectado!");
  }
}
