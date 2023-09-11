package rank;

import java.util.Objects;

public class Link {
  private String origin;
  private String linked;
  public Link(String origin, String linked){
    this.origin = origin;
    this.linked = linked;
  }

  public String getOrigin() {
    return origin;
  }

  public String getLinked() {
    return linked;
  }

  @Override
  public boolean equals(Object obj) {
    boolean same = false;
    if(obj instanceof Link){
      Link otro = (Link) obj;
      same = this.origin.equalsIgnoreCase(otro.origin) &&
          this.linked.equalsIgnoreCase(otro.linked);
    }
    return same;
  }

  @Override
  public int hashCode() {
    return Objects.hash(origin.toLowerCase(), linked.toLowerCase());
  }

  @Override
  public String toString() {
    return String.format("%s->%s",origin,linked);
  }
}
