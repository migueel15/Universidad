/**
 * 
 */
package practica2;

import java.util.Arrays;

import practica1.BinaryTreeCod;
import practica1.TreeException;

/**
 * Practica 2. TIC
 * 
 * @author Jose A Montenegro
 * @version 1.0 8/10/2013
 *
 */
public class Fuente {

	private static final int AddLevel = 3; // ToDo: Obtener valor teorico.
	double[] probabilidades;
	int lengProbabilidades = 0;
	BinaryTreeCod code;
	// Arbol para calcular codigo con los parametros de shannon-fano

	// Valores para shannon-Fanno
	boolean codeSetting = false;
	boolean parametersValid = false;
	int[] parametros;
	int parametroslong;

	HuffmanCode huffmanCodification = null;

	/**********
	 * Constructor de la clase.
	 * Inicialmente ordeno las probabilidades para estar
	 * en orden decreciente.
	 * Ojo puede ser un problema al codificar. Para este
	 * ejercicio no supone ningun problema
	 * 
	 * @param distribution Array de probabilidades
	 */
	public Fuente(double[] distribution) {

		// Ordenan de forma decreciente las probabilidades.
		// ToDo: Sustituir por un algoritmo unico.

		probabilidades = reverseOrder(distribution);

		lengProbabilidades = probabilidades.length;

		parametroslong = (int) logBase2(lengProbabilidades) + AddLevel;
		parametros = new int[parametroslong];
	}

	/**
	 * Calcula la entropia
	 * 
	 * @return Entropia de la distribucion de probabilidad.
	 */
	public double entropia() {
		double h = 0;

		for (int i = 0; i < this.probabilidades.length; i++) {
			double pi = this.probabilidades[i];
			h += pi * logBase2((1 / pi));
		}

		return h;
	}

	/**
	 * Calcula la longitud media de un codigo.
	 * 
	 * Nota: El parametro n1 esta en la posicion cero del array.
	 * 
	 * @param Codigo
	 * @return Longitud media.
	 */
	public double longitudMedia() {
		double l = 0.0;

		for (int i = 0; i < this.lengProbabilidades; i++) {
			l += probabilidades[i] * (int) Math.ceil(-logBase2(probabilidades[i]));
		}

		return l;
	}

	/**
	 * Devuelve los parametros ni para un codigo segun la regla de Shannon-Fano.
	 * 
	 * @return Parametros de un codigo.
	 */
	public int[] reglaDeShannonFano() {

		int[] longitudes = new int[lengProbabilidades];

		// calcula la longitud de cada símbolo
		int maxLongitud = 0;
		for (int i = 0; i < lengProbabilidades; i++) {
			longitudes[i] = (int) Math.ceil(-logBase2(probabilidades[i]));
			if (longitudes[i] > maxLongitud) {
				maxLongitud = longitudes[i];
			}
		}

		// genera el array con los ni
		for (int i = 0; i < lengProbabilidades; i++) {
			int longitud = longitudes[i];
			parametros[longitud - 1]++;
		}

		parametersValid = true;

		return parametros;
	}

	/************
	 * Calcula los codigos mediante arbol binario de practica 1 y
	 * los parametros de Shannon-Fano
	 * 
	 * @return
	 * @throws TreeException
	 */
	public boolean setCodeShannonFano() throws TreeException {

		int[] parameters = reglaDeShannonFano();
		int leng = parameters.length;

		code = new BinaryTreeCod(leng);
		codeSetting = code.buildCodeParameters(parameters);

		return codeSetting;
	}

	/***************
	 * Calcula el codigo optimo mediante la clase Huffman
	 * 
	 * @return
	 */

	public void generarCodigoOptimo() {
		huffmanCodification = new HuffmanCode(probabilidades);
	}

	/*********
	 * Obtengo la longitud optima del codigo huffman.
	 * Si no esta calculado codigo optimo devuelve cero.
	 * 
	 * @return longitudOptima
	 */
	public double longitudOptima() {

		if (huffmanCodification == null)
			return 0.0;

		return huffmanCodification.longitudOptima();
	}

	/********
	 * Imprime el codigo optimo calculado
	 */

	public void printCodeOptima() {

		if (huffmanCodification != null) {
			int huffmanParameters[];
			huffmanParameters = huffmanCodification.getParametrosOptima();
			System.out.println("\nOptimal Code obtained with " +
					Arrays.toString(huffmanParameters) + " parameters:");
			System.out.println(huffmanCodification.getCodeOptima().toString());
		}
	}

	/********
	 * Verifica desigualdades de la pagina 54
	 * 
	 * @param entropia
	 * @param lm
	 * @param lo
	 * @return verdadero si cumple las condiciones
	 */

	public boolean Verificar(double entropia, double lm, double lo) {
		if (entropia < lo & lo < lm & lm < (entropia + 1))
			return true;
		else
			return false;
	}

	/*******
	 * Calculo logartimo base 2 mediante cambio de base
	 * 
	 * @param num
	 * @return
	 */
	private double logBase2(double num) {
		return logCambioBase(num, 2);
	}

	private double logCambioBase(double num, int b) {
		return Math.log(num) / Math.log(b);
	}

	/**********
	 * Ordena de forma descendente las probabilidades
	 * ToDo: Mejorar algoritmo
	 * 
	 * @param distribution
	 * @return
	 */
	private double[] reverseOrder(double distribution[]) {

		Arrays.sort(distribution);

		double temp;

		for (int i = 0; i < distribution.length / 2; i++) {
			temp = distribution[i];
			distribution[i] = distribution[distribution.length - 1 - i];
			distribution[distribution.length - 1 - i] = temp;
		}
		return distribution;
	}

	public static void main(String[] args) {

		// double[] probabilidadesEjemplo5 = { 0.4, 0.2, 0.2, 0.1, 0.1 };
		double[] probabilidadesEjemplo5 = { 0.4, 0.2, 0.2, 0.1, 0.1 };

		Fuente fuente = new Fuente(probabilidadesEjemplo5);
		int[] parameters = fuente.reglaDeShannonFano();

		// Creo codigo con los parametros que calculamos con ShannonFano mediante arbol
		// binario

		BinaryTreeCod code = new BinaryTreeCod(parameters.length);
		boolean result = false;
		try {
			result = code.buildCodeParameters(parameters);

		} catch (TreeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Calculo codigo optimo con huffman

		fuente.generarCodigoOptimo();

		double entropia, lm, lo;
		entropia = fuente.entropia();
		lm = fuente.longitudMedia();
		lo = fuente.longitudOptima();

		System.out.println("Ejercicio 5. Probabilidades: " + Arrays.toString(probabilidadesEjemplo5));

		System.out.println("\nShannon Fano Codigos\n--------------------");
		if (result)
			code.printCodes(parameters);

		System.out.println("\n\n Huffman Codigos\n--------------------");
		fuente.printCodeOptima();

		System.out.println("\n****** Calculo Valores ***********\n");
		System.out.println("Entropia: " + entropia);
		System.out.println("Longitud Media: " + lm);
		System.out.println("Longitud Optima: " + lo);

		System.out.println("Verificar h<lo<lm<h+1: " +
				fuente.Verificar(entropia, lm, lo));

	}

}
