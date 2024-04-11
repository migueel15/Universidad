public class Productor extends Thread{
  private static java.util.Random r = new java.util.Random();
  private int iteraciones;
  private Buffer buffer;

  public Productor(int iteraciones, Buffer buffr){
    buffer = buffr;
    this.iteraciones = iteraciones;
  }

  public void run(){
    int nDato = 0;
    for(int i = 0; i < iteraciones; i++){
      nDato = r.nextInt(100);
      buffer.insertar(nDato);
    }
  }
}
