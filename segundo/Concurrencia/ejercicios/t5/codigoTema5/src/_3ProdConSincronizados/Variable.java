package _3ProdConSincronizados;

public class Variable<T> {
    private T var;
    private boolean hayDato = false;

    public void almacena(T dato){
    	while (hayDato) Thread.yield(); //Condici?n de sincronizaci?n. Espera activa
    	//System.out.println("Productor "+dato);
        var = dato;
        hayDato=true;
    }
    
    public T extrae(){
    	while (!hayDato) Thread.yield();
    	T v = var;
    	//System.out.println("Consumidor "+v);
    	hayDato = false;
        return v;
    }
}
