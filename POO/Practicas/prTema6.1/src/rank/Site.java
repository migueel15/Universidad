package rank;

import java.util.Objects;

public class Site implements Comparable<Site> {
  private String name;
  private double rank;
  public Site(String name){
    this.name = name;
    this.rank = 0;
  }

  public String getName() {
    return name;
  }

  public double getRank() {
    return rank;
  }
  public void addRank(double r){
    this.rank += r;
  }

  @Override
  public boolean equals(Object obj) {
    boolean same = false;
    if(obj instanceof Site){
      Site otro = (Site) obj;
      same = this.name.equalsIgnoreCase(otro.name);
    }
    return same;
  }

  @Override
  public int hashCode() {
    return Objects.hash(name.toLowerCase());
  }

  @Override
  public int compareTo(Site o) {
    return this.name.compareTo(o.name);
  }

  @Override
  public String toString() {
    return String.format("%s(%.5f)",name,rank);
  }
}
