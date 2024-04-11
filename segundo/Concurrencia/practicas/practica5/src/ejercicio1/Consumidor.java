package ejercicio1;
public class Consumidor extends Thread {
  private int iteraciones;
  private Buffer buffer;

  public Consumidor(int iteraciones, Buffer buffr){
    this.iteraciones = iteraciones;
    buffer = buffr;
  }

  public void run(){
    for(int i = 0; i < iteraciones; i++){
      buffer.extraer();
    }
  }

}

