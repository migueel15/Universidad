package ejercicio1;
import java.util.Scanner;
public class Principal {
	public static void main(String[] args) {
		try (Scanner sc = new Scanner(System.in);){
			int bufsz, max;
			System.out.print("Introduce tama�o del buffer: ");
			bufsz = sc.nextInt();
			System.out.print("Introduce maximo del generador: ");
			max = sc.nextInt();

			Buffer buffer = new Buffer(bufsz);
			Productor productor = new Productor(max,buffer);
			Consumidor consumidor = new Consumidor(max,buffer);

			productor.start();
			consumidor.start();

			productor.join();
			consumidor.join();

		} catch (Exception e) {
			e.printStackTrace();
}}}
