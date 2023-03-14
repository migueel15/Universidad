package jarras;

public class Jarra {

  private int contenido;

  private final int capacidad;

  public Jarra(int capacidad){
    if(capacidad <= 0){
      throw new RuntimeException("Capacidad no valida");
    }else{
      this.capacidad = capacidad;
    }
  }
   public int capacidad(){
     return  capacidad;
   }

  public int contenido(){
     return contenido;
  }

  public void llena(){
    contenido = capacidad;
  }
  public void vacia(){
     contenido = 0;
  }

  public void llenaDesde(Jarra jarra){
     if(jarra != this){
       int jarraHueco = this.capacidad - this.contenido;
       if(jarraHueco <= jarra.contenido){
         this.llena();
         jarra.contenido -= jarraHueco;
       }else{
         this.contenido += jarra.contenido;
         jarra.vacia();
       }
     }else {
       throw new RuntimeException("Error");
     }
  }

  @Override
  public String toString(){
    return "J(" + capacidad + ", "+ contenido + ")";
  }
}