package primos;

public class Primos {
  private long a, b;
  private int pos;

  public Primos(long a, long b, int pos) {
    this.a = a;
    this.b = b;
    this.pos = pos;
  }

  public String toString() { return pos + ":(" + a + "," + b + ")"; }

  public long getA() { return a; }
  public long getB() { return b; }
  public int getPos() { return pos; }
}
