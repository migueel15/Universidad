package _2ProdConsSemaforosBinarios;

import java.util.concurrent.Semaphore;

//Buffer sincronizado utilizando semaforos
public class Buffer {
  private int[] elem; // array de elementos
  private int nelem; // numero de elementos en el buffer
  private int p; // posición donde guardar próximo elemento
  private int c; // posición donde está el siguiente elemento a consumir

  private Semaphore mutex = new Semaphore(1);
  private Semaphore estaLleno = new Semaphore(0);
  private Semaphore hayHueco;

  public Buffer(int n) {
    if (n <= 0) {
      throw new IllegalArgumentException();
    }

    nelem = 0;

    hayHueco = new Semaphore(n);
    elem = new int[n];
    p = 0;
    c = 0;
  }

  public void insertar(int x) throws InterruptedException {
    // Condición de sincronización - si el buffer está lleno espero
    hayHueco.acquire();
    // COMPLETAR
    mutex.acquire();
    // ------SC-----
    elem[p] = x;
    p = (p + 1) % elem.length; // incremento circular

    ++nelem;
    // COMPLETAR
    System.out.println("Elemento Producido: " + x);
    System.out.print("nelem: " + nelem);
    // System.out.print(", hayDatos: " + hayDatos.availablePermits());
    // System.out.println(", hayHuecos: " + hayHuecos.availablePermits());
    mutex.release();
    estaLleno.release();
  }

  public int extraer() throws InterruptedException {
    // Condición de sincronización - si el buffer está vacío espero
    estaLleno.acquire();
    mutex.acquire();
    // ------SC------
    int x = elem[c];
    c = (c + 1) % elem.length; // incremento circular
    --nelem;
    // COMPLETAR
    System.out.println("Elemento Consumido: " + x);
    System.out.print("nelem: " + nelem);
    // System.out.print(", hayDatos: " + hayDatos.availablePermits());
    // System.out.println(", hayHuecos: " + hayHuecos.availablePermits());
    mutex.release();
    hayHueco.release();
    return x;
  }
}
