import datos.Datos;
import datos.DatosException;

import java.util.Arrays;

public class PruebaDatos {
  public static void main(String[] args) {
    if (args.length < 2){
      System.out.println("Error, no hay valores suficientes");
    }else{
      try{
        String[] datosString = Arrays.copyOfRange(args,2,args.length);
        Datos datos = new Datos(datosString, Double.parseDouble(args[0]), Double.parseDouble(args[1]));

        System.out.println(datos);

        datos.setRango("0;4");

        System.out.println(datos);

        datos.setRango("10 25");
      }catch (DatosException e){
        System.out.println(e.getMessage());
      }catch (NumberFormatException e){
        System.out.println("Error, al convertir un valor a número real (" + e.getMessage() +")");
      }
    }

  }
}
