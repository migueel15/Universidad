import cuentapalabras.PalabraEnTexto;

public class PruebaPalabraEnTexto {
  public static void main(String[] args) {
    PalabraEnTexto gorra = new PalabraEnTexto("gorra");
    PalabraEnTexto gorraM = new PalabraEnTexto("GORRA");

    gorra.incrementa();

    System.out.println("Palabra 1 = " + gorra);
    System.out.println("Palabra 2 = " + gorraM);

    if(gorra.equals(gorraM)){
      System.out.println("Las palabras son iguales");
    }else{
      System.out.println("Las palabras no son iguales");
    }
  }
}
