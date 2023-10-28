package ejercicios;

public class ArraysConRepeticiones {

	//Precondición: hay un elemento repetido=> v.length >= 2
	public static int encuentraElem(int [] v) {
		return encuentraElem(v,0,v.length-1);
	}
	
	private static int encuentraElem(int [] v, int izq, int der) {
    int pos;
    if(izq != der) {
      pos = (izq+der-1)/2;
    }else{
      pos = izq;
    }
    // si está en su sitio buscamos a la derecha
    if(pos == v[pos]){
      return encuentraElem(v,pos+1,der);
    }else{ // si no está en su sitio el duplicado está a la izquierda
      if(pos >= 1 && v[pos] == v[pos-1]){
        return v[pos];
      }else{
        return encuentraElem(v,izq,pos);
      }
    }
	}

  public static void main(String[] args) {
    int[] lista = {0,0};
    int valor = encuentraElem(lista);
    System.out.println(valor);
  }
}
