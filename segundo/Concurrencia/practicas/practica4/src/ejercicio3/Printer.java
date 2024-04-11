package ejercicio3;

public class Printer extends Thread{
  VariableCompartida var;

  public Printer(VariableCompartida var){
    this.var = var;
  }

  public void run(){
    for(int i = 0; i < 100; i++){
      System.out.println(var.getV());
    }
  }
}
