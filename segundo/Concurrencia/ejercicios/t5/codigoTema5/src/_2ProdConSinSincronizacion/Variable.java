package _2ProdConSinSincronizacion;

public class Variable<T> {
  private T var;

  /*
   * En este caso es necesario definir el constructor
   * e inicializar la variable porque si no Java
   * ejecuta el constructor por defecto, donde inicializa
   * var a null por ser una referencia a un objeto
   * 
   * Si el consumidor lee la variable antes de que el productor
   * ponga un primer valor --> NullPointerException
   */
  public Variable(T var) {
    this.var = var;
  }

  public void almacena(T dato) {
    var = dato;
  }

  public T extrae() {
    return var;
  }
}
