package tema5;

import java.util.ArrayList;
import java.util.Arrays;

public class VorazMochila {
  private int[] pesos;
  private int[] valores;
  public VorazMochila(int[] pesos, int[]valores){
    this.pesos = pesos;
    this.valores = valores;
  }
  public int[] encontrarValores(int pesoMochila){
    int maxValor = 0;
    int pesoRestante = pesoMochila;
    int[] respuesta = new int[pesos.length];
    int i = 0;
    while (i < pesos.length){
      if(pesos[i] <= pesoRestante){
        respuesta[i] = 1;
        pesoRestante -= pesos[i];
      }
      i++;
    }

    return respuesta;
  }

  public static void main(String[] args) {
    int p[] = {5,4,3,2,1};
    int v[] = {3,4,5,6,7};
    VorazMochila mochila = new VorazMochila(p,v);
    int[] res = mochila.encontrarValores(10);
    System.out.println(Arrays.toString(res));
  }
}
