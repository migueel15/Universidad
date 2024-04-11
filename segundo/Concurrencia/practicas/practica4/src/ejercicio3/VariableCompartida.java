package ejercicio3;

public class VariableCompartida {
  int v;
  int mostrar;

  public VariableCompartida(){
    v = 0;
    mostrar = 1;
  }
  public void setV(int v) {
    while(mostrar == 1){
      Thread.yield();
    }
    mostrar = 1;
    if(v <= 99 &&  v >= 0){
      this.v = v;
    }
  }
  public int getV() {
    while(mostrar == 0){
      Thread.yield();
    }
    mostrar = 0;
    return v;
  }
  public void inc(){
    setV(v+1);
  }
}
