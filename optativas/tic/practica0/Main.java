/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package practica0;

import practica0.Codificacion;
import practica0.ISBN;
import practica0.NIF;
import practica0.UPC;

/**
 *
 * @author Monte
 */
public class Main {

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {

		String codigoISBN = "157955008"; // 1579550088 ;
		String codigoISBN13 = "978-1-84800-272-"; // 978-1-84800-272-2;
		String codigoNIF = "55555555"; // 55555555K
		String codigoUPC = "9780444485199";

		/**********************
		 * 
		 */
		Codificacion cod = new ISBN();

		codigoISBN = cod.generarCodigoControl(codigoISBN);
		System.out.print("ISBN: " + codigoISBN + ": ");
		System.out.println(cod.verificar(codigoISBN) ? "valido" : "No valido");

		/*********************************
		 *
		 */
		cod = new ISBN13();
		codigoISBN13 = cod.generarCodigoControl(codigoISBN13);
		System.out.print("ISBN13: " + codigoISBN13 + ": ");
		System.out.println(cod.verificar(codigoISBN13) ? "valido" : "No valido");

		/*********************************
		 * 
		 */
		cod = new NIF();
		codigoNIF = cod.generarCodigoControl(codigoNIF);
		System.out.print("NIF: " + codigoNIF + ": ");
		System.out.println(cod.verificar(codigoNIF) ? "valido" : "No valido");

		/*****************
		 * 
		 */
		cod = new UPC();
		System.out.print("UPC: " + codigoUPC + ": ");
		System.out.println(cod.verificar(codigoUPC) ? "valido" : "No valido");

	}

}
