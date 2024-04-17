package _0JardinSemaforos;

public class Puerta extends Thread{
    private Contador visitantes;
    private int iter;
    private String id;
    
    public Puerta(String id, Contador c, int iter){
         visitantes = c;
         this.iter = iter;
         this.id=id;
         System.out.println("Por la puerta " + this.id + " se esperan " + iter + " visitantes");
    }

    public void run(){
         for (int i = 0; i< iter; i++){
              try {
				visitantes.inc();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
         }
   }
}
