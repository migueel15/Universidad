package _5JardinesPetersonV2_clase;

public class Puerta extends Thread{
    private Contador visitantes;
    private int iter;
    private int id;
    
    public Puerta(int id, Contador c, int iter){
         visitantes = c;
         this.iter = iter;
         this.id=id;
         System.out.println("Por la puerta P" + this.id + " se esperan " + iter + " visitantes");
    }

    public void run(){
         for (int i = 0; i< iter; i++){
        	 if (id == 1){
        		 visitantes.inc();
        	 }else if (id == 2){
        		 visitantes.inc();
        	 }
          }
   }
}
