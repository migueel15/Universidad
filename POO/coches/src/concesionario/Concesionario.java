package concesionario;

import java.util.ArrayList;

public class Concesionario {
  private ArrayList<Coche> coches;

  public Concesionario(){
    coches = new ArrayList<>();
  }

  public void anyadir(Coche coche){
    int posicion = buscarCoche(coche.getModelo());
    if (posicion != -1){
      coches.set(posicion, coche);
    }else{
      coches.add(coche);
    }
  }

  public void eliminar(String modelo){
    int posicion = buscarCoche(modelo);
    if (posicion != -1){
      coches.remove(posicion);
    }else{
      throw new RuntimeException("Coche no encontrado.");
    }
  }

  private int buscarCoche(String modelo){
    boolean exists = false;
    int contador = 0;
    while (contador < coches.size() && !exists){
      if(coches.get(contador).getModelo().equalsIgnoreCase(modelo)){
        exists = true;
      }
      else{
        contador++;
      }
    }
    return contador < coches.size() ? contador : -1;
  }

  public Coche cocheMasBarato(){
    if(coches.isEmpty()){
      throw new RuntimeException("Lista de coches vacía");
    }

    double precioMenor = coches.get(0).calcPrecioFinal();
    int pos = 0;

    for( Coche coche : coches){
      if(coche.calcPrecioFinal() < precioMenor){
          precioMenor = coche.calcPrecioFinal();
          pos = coches.indexOf(coche);
        }
      }
    return coches.get(pos);
  }

  public ArrayList<Coche> seleccionarPrecio(double precioMin, double precioMax){
    ArrayList<Coche> listaCochesFiltrada = new ArrayList<>();
    for(Coche coche: coches){
      if (coche.calcPrecioFinal() < precioMax && coche.calcPrecioFinal() >= precioMin){
        listaCochesFiltrada.add(coche);
      }
    }
    return listaCochesFiltrada;
  }

  @Override
  public String toString() {
    return coches.toString();
  }
}
