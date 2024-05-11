package lib;

import java.io.IOException;
import java.net.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class Servidor {
  private int port;
  private DatagramSocket ds = null;
  private DatagramPacket dp = null;
  private  byte[] buffer = new byte[1000];

  public Servidor(int port){
    this.port = port;
    try {
      ds = new DatagramSocket(this.port);
      System.out.println("Servidor escuchando en el puerto " + this.port);
    } catch (SocketException e) {
      System.out.println("No se ha podido crear el Socket");
    }
  }

  public String recieveData(){
    dp = new DatagramPacket(buffer,buffer.length);
    try {
      ds.receive(dp);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    String recibido = new String(dp.getData(),dp.getOffset(),dp.getLength()
        , Charset.forName("UTF-8"));
    showMessage(dp.getAddress(),recibido);
    return recibido;
  }

  private void showMessage(InetAddress ip, String message){
    if(Character.isDigit(message.charAt(0))){
      System.out.println(ip + ": " + message.substring(1));
    }else{
      System.out.println(ip + ": " + message);
      System.out.println(ip + " se ha desconectado.");
    }
  }

  private String filterMessage(String message){
    ArrayList<String> resultado = new ArrayList<>();
    if(Character.isDigit(message.charAt(0))){
      int maxChars = Integer.parseInt(message.substring(0,1));
      String[] data = message.substring(1).split(" ");
      // remove items in data that are longer than maxChars
      for (int i = 0; i < data.length; i++) {
        if (data[i].length() >= maxChars) {
          resultado.add(data[i]);
        }
      }
      return String.join(" ", resultado);
    }else {
      return message;
    }
  }

  public void sendData(String data) {
    dp = new DatagramPacket(data.getBytes(StandardCharsets.UTF_8),
        data.length(),
        dp.getAddress(),
        dp.getPort());

		try {
      ds.send(dp);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void closeConnection(){
    ds.close();
  }

  public void start(){
    while(true){
      String data = recieveData();
      sendData(filterMessage(data));
    }
	}
}
