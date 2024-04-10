package _6JardinesDekker;

public class Puerta extends Thread{
	private Dekker dekker;
    private Contador visitantes;
    private int iter;
    private int id;
    
    public Puerta(int id, Contador c, Dekker dekker, int iter){
         visitantes = c;
         this.iter = iter;
         this.id=id;
         this.dekker = dekker;
         System.out.println("Por la puerta P" + this.id + " se esperan " + iter + " visitantes");
    }

    public void run(){
    	for (int i = 0; i < iter; i++) {
			dekker.entrar(id); //Protocolo de entrada
			visitantes.inc();
			dekker.salir(id);  //Protocolo de salida
		}
   }
}
