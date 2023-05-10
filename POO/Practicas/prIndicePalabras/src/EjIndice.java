
import indices.*;

public class EjIndice {
    public static void main(String args[]) {
		String delimitadores = "[ .,:;\\-\\!\\!\\?\\?]+";
        //Indice cp = new IndiceContador();
        //Indice cp = new IndiceLineas();
        Indice cp = new IndicePosicionesEnLineas();
        cp.agregarFrase("Guerra tenia una jarra y Parra tenia una perra, "
                + "pero la perra de Parra rompio la jarra de Guerra.");
        cp.agregarFrase("Guerra pego con la porra a la perra de Parra. "
                + "!Oiga usted buen hombre de Parra! "
                + "Por que ha pegado con la porra a la perra de Parra.");
        cp.agregarFrase("Porque si la perra de Parra no hubiera roto "
                + "la jarra de Guerra, "
                + "Guerra no hubiera pegado con la porra "
                + "a la perra de Parra.");
        cp.resolver(delimitadores);
        cp.presentarIndiceConsola();
    }
}
