import java.util.Arrays;

public class TarifaTelefonica {

  private int tarifaPlana, permanencia, tarifaMegas;
  private int[] estimacion;
  private int[] pago; // Pago mínimo a realizar durante los meses de i...n

  public TarifaTelefonica(int tp, int p, int tm, int[] est) {
    tarifaPlana = tp;
    permanencia = p;
    tarifaMegas = tm;
    estimacion = est;
    pago = null;
  }

  public int resolverBottomUp() {
    int n = estimacion.length;
    pago = new int[n + 1];
    pago[n] = 0;

    for (int i = n - 1; i >= 0; i--) {
      pago[i] = tarifaMegas * estimacion[i] + pago[i + 1];

      if (i + permanencia <= n &&
          pago[i] > permanencia * tarifaPlana + pago[i + permanencia]) {
        pago[i] = permanencia * tarifaPlana + pago[i + permanencia];
      }
    }
    return pago[0];
  }

  public int[] reconstruirSol() {
    if (pago == null) {
      throw new RuntimeException("Se debe resolver el problema primero");
    }
    int[] res = new int[estimacion.length];
    for (int i = 0; i < estimacion.length; i++) {
      if (pago[i] == pago[1 + i] + tarifaMegas * estimacion[i]) {
        res[i] = 0;
      } else {
        for (int j = 0; j < permanencia; j++) {
          res[i + j] = 1;
        }
        i += permanencia - 1;
      }
    }
    return res;
  }

  public void imprimeVectorSolucion() {
    System.out.println(Arrays.toString(pago));
  }
}
