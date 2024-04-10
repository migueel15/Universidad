package _4JardinesPetersonV1;

public class Puerta extends Thread{
	private Peterson peterson;
    private Contador visitantes;
    private int iter;
    private int id;
    
    public Puerta(int id, Contador c, Peterson peterson, int iter){
         visitantes = c;
         this.iter = iter;
         this.id=id;
         this.peterson = peterson;
         System.out.println("Por la puerta P" + this.id + " se esperan " + iter + " visitantes");
    }

    public void run(){
         for (int i = 0; i< iter; i++){
        	 if (id == 1){
        		 peterson.entrarP1();
        		 visitantes.inc();
        		 peterson.salirP1();
        	 }else if (id == 2){
        		 peterson.entrarP2();
        		 visitantes.inc();
        		 peterson.salirP2();
        	 }
          }
   }
}
