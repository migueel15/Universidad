package ejercicio2;

public class Main {
  public static void main(String[] args) throws InterruptedException {
    VariableCompartida var = new VariableCompartida();
    Hebra h1 = new Hebra(var);
    Hebra h2 = new Hebra(var);

    h1.start();
    h2.start();

    h1.join();
    h2.join();

    System.out.println(var.getV());

    // Con 10 iteraciones el valor es correcto. Una vez paso de 300
    // iteraciones no siempre se obtiene el resultado esperado. Esto ocurre
    // porque tanto la hebra 1 como la 2 acceden simultaneamente a la
    // variable y se pierde un incremento.
  }
}
