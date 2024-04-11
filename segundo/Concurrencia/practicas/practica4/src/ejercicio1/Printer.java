package ejercicio1;

public class Printer extends Thread{
  char caracter;
  int iteraciones;
  public Printer(char character, int iteraciones){
    caracter = character;
    this.iteraciones = iteraciones;
  }
  public void run(){
    for(int i = 0; i < iteraciones; i++){
      System.out.println(caracter);
    }
  }
}
