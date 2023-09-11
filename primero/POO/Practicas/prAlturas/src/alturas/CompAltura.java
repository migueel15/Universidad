package alturas;

import java.util.Comparator;

public class CompAltura implements Comparator<Pais> {
  @Override
  public int compare(Pais o1, Pais o2) {
    int value = Double.compare(o1.getAltura(),o2.getAltura());
    if(value == 0){
      value = o1.getNombre().compareTo(o2.getNombre());
    }
    return value;
  }
}
