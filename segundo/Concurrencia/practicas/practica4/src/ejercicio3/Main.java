package ejercicio3;

public class Main {
  public static void main(String[] args) throws InterruptedException {
    VariableCompartida var = new VariableCompartida();

    Actualizador actualizador = new Actualizador(var);
    Printer printer = new Printer(var);

    actualizador.start();
    printer.start();

    actualizador.join();
    printer.join();

    // Se muestra 99 veces el último valor ingresado
    // Para solucionarlo he creado una variable que gestionará el orden de
    // acceso al valor actual para que ambos no puedan acceder a la vez y
    // solo cuando se haya leido podrá actualizarse.

  }
}
