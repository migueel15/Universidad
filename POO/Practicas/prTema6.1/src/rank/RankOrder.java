package rank;

import java.util.Comparator;

public class RankOrder implements Comparator<Site> {
  @Override
  public int compare(Site o1, Site o2) {
    int order = Double.compare(o1.getRank(), o2.getRank())*-1;
    if(order == 0){
      order = o1.compareTo(o2);
    }
    return order;
  }
}
