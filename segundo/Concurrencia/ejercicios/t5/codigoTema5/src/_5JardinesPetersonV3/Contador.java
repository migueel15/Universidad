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
    //No se está implementando ningún control de acceso en este caso
    //Si se puede consultar el valor al mismo tiempo que se está incrementando
    //habría que asegurar la exclusión mutua también en este método
    public int valor(){
        return cont;
    }
}
