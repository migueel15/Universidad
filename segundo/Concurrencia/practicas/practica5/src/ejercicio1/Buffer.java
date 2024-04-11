package ejercicio1;
public class Buffer {
	private int[] elem; //array de elementos
	private int nelem;  //n�mero de elementos en el buffer
	private int p;      //posici�n donde guardar pr�ximo elemento
	private int c;      //posici�n donde est� el siguiente elemento a consumir

	Peterson peterson;
	
	public Buffer(int n) {
		if (n <= 0) {
			throw new IllegalArgumentException();
		}
		elem = new int[n];
		p = 0;
		c = 0;
		nelem = 0;
		peterson = new Peterson();
	}

	public void insertar(int x) {
		peterson.entrada_productor();
		while(nelem == elem.length){
			Thread.yield();
		}
		elem[p] = x;
		System.out.println("Productor: "+x);
		p = (p+1) % elem.length; //incremento circular
		++nelem;
		peterson.salida_productor();
	}
	
	public int extraer() {
		peterson.entrada_consumidor();
		while(nelem == 0){
			Thread.yield();
		}
		int x = elem[c];
		System.out.println("	Consumidor: "+x);
		c = (c+1) % elem.length; //incremento circular
		--nelem;
		peterson.salida_consumidor();
		return x;
	}
}
