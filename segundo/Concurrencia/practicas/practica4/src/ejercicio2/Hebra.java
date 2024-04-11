package ejercicio2;

public class Hebra extends Thread{
  VariableCompartida var;

  public Hebra(VariableCompartida var){
    this.var = var;
  }

  public void run(){
    for(int i = 0; i < 300; i++){
      var.inc();
    }
  }
}
