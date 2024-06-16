package primos;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import javax.swing.*;

public class Worker extends SwingWorker<java.util.List<Primos>, Void> {
  private int n;
  private Panel panel;
  private int type; // 2 = twin, 4 = cousin, 6 = sexy

  public Worker(int n, Panel panel, int type) {
    this.n = n;
    this.panel = panel;
    this.type = type;
  }

  @Override
  protected java.util.List<Primos> doInBackground() throws Exception {
    long a = 3;
    long b;
    int pos = 0;
    List<Primos> primos = new ArrayList<Primos>();
    while (!isCancelled() && primos.size() < n) {

      b = a + type;
      if (esPrimo(a) && esPrimo(b)) {
        primos.add(new Primos(a, b, pos++));
      }
      a += 2;
    }
    return primos;
  }

  @Override
  protected void done() {
    try {
      java.util.List<Primos> primos = get();
      if (primos.size() > 0) {
        if (type == 2) {
          panel.limpiaAreaTwin();
          panel.mensajeTwin("Tarea terminada");
          panel.escribePrimosTwin(primos);
        } else if (type == 4) {
          panel.limpiaAreaCousin();
          panel.mensajeCousin("Tarea terminada");
          panel.escribePrimosCousin(primos);
        } else if (type == 6) {
          panel.limpiaAreaSexy();
          panel.mensajeSexy("Tarea terminada");
          panel.escribePrimosSexy(primos);
        }
      }
    } catch (InterruptedException e) {
      System.out.println("tarea cancelada");
      e.printStackTrace();
    } catch (ExecutionException | CancellationException e){
      System.out.println("tarea cancelada");
    }
  }

  private boolean esPrimo(long n) {
    if (n <= 1) {
      return false;
    }
    for (int i = 2; i <= Math.sqrt(n); i++) {
      if (n % i == 0) {
        return false;
      }
    }
    return true;
  }
}
