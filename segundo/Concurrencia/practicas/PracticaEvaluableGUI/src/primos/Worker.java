package primos;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import javax.swing.*;

public class Worker extends SwingWorker<Void, Primos> {
  private int n;
  private Panel panel;
  private int type; // 2 = twin, 4 = cousin, 6 = sexy

  public Worker(int n, Panel panel, int type) {
    this.n = n;
    this.panel = panel;
    this.type = type;
    if (type == 2) {
      panel.limpiaAreaTwin();
    } else if (type == 4) {
      panel.limpiaAreaCousin();
    } else if (type == 6) {
      panel.limpiaAreaSexy();
    }
  }

  public int getType() { return type; }

  @Override
  protected Void doInBackground() throws Exception {
    this.setProgress(0);
    int cantidad = 0;
    long a = 3;
    long b;
    int pos = 0;
    while (!isCancelled() && cantidad < n) {
      b = a + type;
      if (esPrimo(a) && esPrimo(b)) {
        publish(new Primos(a, b, pos++));
        cantidad++;
        this.setProgress((cantidad * 100) / n);
      }
      a += 2;
    }
    return null;
  }

  @Override
  protected void done() {
    try {
      get();
    } catch (InterruptedException e) {
      System.out.println("tarea cancelada");
      e.printStackTrace();
    } catch (ExecutionException | CancellationException e) {
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

  public void process(List<Primos> lista) {
    try {
      if (type == 2) {
        panel.escribePrimosTwin(lista);
        panel.mensajeTwin("Numeros calculados");
      } else if (type == 4) {
        panel.escribePrimosCousin(lista);
        panel.mensajeCousin("Numeros calculados");
      } else if (type == 6) {
        panel.escribePrimosSexy(lista);
        panel.mensajeSexy("Numeros calculados");
      }
    } catch (CancellationException e) {
      System.out.println("Error en el proceso");
    }
  }
}
