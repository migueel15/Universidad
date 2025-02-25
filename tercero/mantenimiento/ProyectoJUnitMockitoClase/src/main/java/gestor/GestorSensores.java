package gestor;

import java.util.ArrayList;
import java.util.List;

public class GestorSensores {

    private List<String> sensores;

    public GestorSensores(){
        sensores = new ArrayList<String>();
    }

    public int getNumeroSensores(){
        return sensores.size();
    }

    public boolean borrarSensor(String sensor){
        if (!sensores.contains(sensor)){
            throw new IllegalArgumentException("Sensor no existe");
        }
        return sensores.remove(sensor);
    }

    public void agregarSensor(String sensor){
        if (sensores.contains(sensor)){
            throw new IllegalArgumentException("Sensor ya existe");
        }
        sensores.add(sensor);
    }

    private boolean containsSensor(String sensor){
        return sensores.contains(sensor);
    }
    
}
