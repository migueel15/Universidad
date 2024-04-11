package ejercicio3;

public class Actualizador extends Thread{
  VariableCompartida var;

  public Actualizador(VariableCompartida var){
    this.var = var;
  }

  public void run(){
    for(int i = 0; i < 99; i++){
      var.inc();
    }
  }
}
