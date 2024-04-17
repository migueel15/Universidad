package _2ProdConsSemaforosGenerales;
// Productor-Consumidor. Exclusión Mutua con Peterson

// Solución con  Buffer sincronizado (exclusión mutua interna al buffer)

import java.util.Scanner;
public class Principal {
	public static void main(String[] args) {
		try (Scanner sc = new Scanner(System.in);){
			int bufsz, max;
			System.out.print("Introduce tama�o del buffer: ");
			bufsz = sc.nextInt();
			System.out.print("Introduce maximo del generador: ");
			max = sc.nextInt();
			
			Buffer b = new Buffer(bufsz);
			Thread t1 = new Thread(new Productor(b, max));
			Thread t2 = new Thread(new Consumidor(b, max));
			
			t1.start();
			t2.start();
			
			t1.join();
			t2.join();
		} catch (Exception e) {
			e.printStackTrace();
}}}
