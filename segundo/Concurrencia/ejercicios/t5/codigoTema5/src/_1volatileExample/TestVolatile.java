package _1volatileExample;

public class TestVolatile {
  private volatile static int myInt = 0;
  // private static int myInt = 0;

  public static void main(String[] args) {
    ThreadListener t1 = new ThreadListener();
    ThreadModifyer t2 = new ThreadModifyer();
    t1.start();
    t2.start();

    try {
      t1.join();
      t2.join();
    } catch (InterruptedException e) {
      ;
    }
  }

  static class ThreadListener extends Thread {
    public void run() {
      int local_value = myInt;
      while (local_value < 5) {
        if (local_value != myInt) {
          System.out.println("Listener:" + myInt);
          local_value = myInt;
        }
      }
    }
  }

  static class ThreadModifyer extends Thread {
    public void run() {
      int local_value = myInt;
      while (local_value < 5) {
        local_value++;
        myInt = local_value;
        System.out.println("Modifyer:" + myInt);
        try {
          Thread.sleep(1000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }
  }
}
