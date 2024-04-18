package mrusa.semaforosV2;

public class Principal {
	public static void main(String[] args) {
		Coche c = new Coche(); //Un coche
		
		Pasajero[] pas = new Pasajero[10]; //10 pasajeros
		for (int i = 0; i<pas.length; i++){
			pas[i] = new Pasajero(i,c);
		}
		ControlAtraccion control = new ControlAtraccion(c);
		
		control.start();
		
		for (int i = 0; i<pas.length; i++){
			pas[i].start();
		}
		
		//Interrupciones
		try {
			Thread.sleep(3000);
			for (int i = 0; i<pas.length; i++){
				pas[i].interrupt();
			}
			control.interrupt();
			for (int i = 0; i<pas.length; i++){
				pas[i].join();
			}
			control.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("fin del programa");
	}
}
