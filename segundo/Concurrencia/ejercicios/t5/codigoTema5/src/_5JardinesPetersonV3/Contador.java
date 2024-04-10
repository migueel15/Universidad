package _5JardinesPetersonV3;

public class Contador {
    private int cont = 0;
    private Peterson peterson = new Peterson();

    public void inc(int id){
    	peterson.entrar(id);
        cont++;
        peterson.salir(id);
    }

    //Solo se llama desde la hebra principal
    //No se est� implementando ning�n control de acceso en este caso
    //Si se puede consultar el valor al mismo tiempo que se est� incrementando
    //habr�a que asegurar la exclusi�n mutua tambi�n en este m�todo
    public int valor(){
        return cont;
    }
}
