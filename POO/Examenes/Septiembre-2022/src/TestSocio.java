import sociedad.Socio;

import java.util.HashSet;
import java.util.Set;

public class TestSocio {
  public static void main(String[] args) {
    Set<String> intereses = new HashSet<>();
    intereses.add("Senderismo");
    intereses.add("Escalada");
    Socio soc1 = new Socio("Layton kor", intereses, 24);
    Socio soc2 = new Socio("layton kor", new HashSet<String>(), 24);

    System.out.println(soc1);
    System.out.println(soc2);
    System.out.println(soc1.equals(soc2));
  }
}
