package jarras;

public class Mesa {
  private Jarra jarra1;
  private Jarra jarra2;
  public Mesa(Jarra j1, Jarra j2){
    if (j1 == j2){
      throw new RuntimeException();
    }else{
      jarra1 = j1;
      jarra2 = j2;
    }
  }

  public Mesa(int cap1, int cap2){
    jarra1 = new Jarra(cap1);
    jarra2 = new Jarra(cap2);
  }

  public int capacidad(int id){
    return switch (id) {
      case 1 -> jarra1.capacidad();
      case 2 -> jarra2.capacidad();
      default -> throw new RuntimeException("Invalid ID");
    };
  }

  public int contenido(int id){
    return switch (id){
      case 1 -> jarra1.contenido();
      case 2 -> jarra2.contenido();
      default -> throw new RuntimeException("Invalid ID");
    };
  }

  public void llena(int id){
    switch (id){
      case 1 -> jarra1.llena();
      case 2 -> jarra2.llena();
      default -> throw new RuntimeException("Invalid ID");
    }
  }

  public void vacia(int id){
    switch (id){
      case 1 -> jarra1.vacia();
      case 2 -> jarra2.vacia();
      default -> throw new RuntimeException("Invalid ID");
    }
  }

  public void llenaDesde(int id){
    switch (id){
      case 1 ->
          jarra2.llenaDesde(jarra1);
      case 2 ->
          jarra1.llenaDesde(jarra2);
      default -> throw new RuntimeException("Invalid ID");
    }
  }

  @Override
  public String toString() {
    return "M("+jarra1.toString() + ", " + jarra2.toString()+")";
  }
}


















