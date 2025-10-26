package practica2;

import java.util.*;

/**
 * Practica 2. TIC
 * 
 * @author Jose A Montenegro
 * @version 1.0 8/10/2013
 * 
 *          Algoritmo basado en la version
 *          http://rosettacode.org/wiki/Huffman_coding#Java
 *
 */

public class HuffmanCode {
	double[] probabilidades;
	HuffmanTree tree;
	List<Values> CodeList;

	public HuffmanCode(double[] probabilidadesP) {
		probabilidades = probabilidadesP;

		tree = regla1(probabilidades);
		CodeList = new ArrayList<Values>();
		CodeList = regla2(tree, new StringBuffer(), CodeList);

	}

	/****************
	 * 
	 * @param probabilidades
	 * @return
	 */
	public HuffmanTree regla1(double[] probabilidades) {

		PriorityQueue<HuffmanTree> trees = new PriorityQueue<HuffmanTree>();

		for (int i = 0; i < probabilidades.length; i++)
			trees.add(new HuffmanLeaf(probabilidades[i]));

		assert trees.size() > 0;

		while (trees.size() > 1) {

			// Optiene los dos elementos menores
			// Estan ordenados
			HuffmanTree a = trees.poll();
			HuffmanTree b = trees.poll();
			// Crea un nodo nuevo con los dos anteriores
			trees.offer(new HuffmanNode(a, b));
		}
		return trees.poll();
	}

	/*********
	 * 
	 * @param tree
	 * @param prefix
	 * @param code
	 * @return
	 */

	public List<Values> regla2(HuffmanTree tree, StringBuffer prefix, List<Values> code) {

		assert tree != null;
		Values value;

		if (tree instanceof HuffmanLeaf) {
			HuffmanLeaf leaf = (HuffmanLeaf) tree;
			value = new Values(prefix.toString(), leaf.frequency);
			code.add(value);

		} else if (tree instanceof HuffmanNode) {
			HuffmanNode node = (HuffmanNode) tree;

			prefix.append('0');
			code = regla2(node.left, prefix, code);
			prefix.deleteCharAt(prefix.length() - 1);

			prefix.append('1');
			code = regla2(node.right, prefix, code);
			prefix.deleteCharAt(prefix.length() - 1);
		}
		return code;
	}

	/**********
	 * Obtiene los codigos
	 * 
	 * @return
	 */
	public List<Values> getCode() {
		return CodeList;
	}

	/*********
	 * Calcula la longitud optima
	 * 
	 * @return
	 */
	public double longitudOptima() {
		double l = 0.0;

		Iterator<Values> itr = CodeList.iterator();

		while (itr.hasNext()) {
			Values element = (Values) itr.next();
			l += element.frecuencia * element.size;
		}
		return l;
	}

	/************
	 * Obtiene los parametros optimos
	 * 
	 * @return
	 */
	public int[] getParametrosOptima() {
		int parameters[] = new int[10];
		Iterator<Values> itr = CodeList.iterator();

		while (itr.hasNext()) {
			Values element = (Values) itr.next();
			parameters[element.size - 1]++;
		}
		return parameters;
	}

	/**********
	 * Obtiene los codigos de huffman
	 * 
	 * @return
	 */
	public List<String> getCodeOptima() {
		List<String> code = new ArrayList<String>();
		Iterator<Values> itr = CodeList.iterator();

		while (itr.hasNext()) {
			Values element = (Values) itr.next();
			code.add(element.code);
		}
		return code;
	}

	/********
	 * Clases Auxiliares para construir el arbol Huffman
	 *
	 */

	class HuffmanTree implements Comparable<HuffmanTree> {
		public double frequency; // the frequency of this tree

		public HuffmanTree(double freq) {
			frequency = freq;
		}

		// compares on the frequency
		public int compareTo(HuffmanTree tree) {
			return (int) (frequency * 100 - tree.frequency * 100);
		}

		public String toString() {
			return frequency + " ";
		}
	}

	class HuffmanLeaf extends HuffmanTree {
		public HuffmanLeaf(double freq) {
			super(freq);
		}
	}

	class HuffmanNode extends HuffmanTree {
		public final HuffmanTree left, right; // subtrees

		public HuffmanNode(HuffmanTree l, HuffmanTree r) {
			super(l.frequency + r.frequency);
			left = l;
			right = r;
		}

	}

	/*******
	 * Clase que devuelve los codigos con sus probabilidades
	 * y longitud, utilizadas para calcular los parametros.
	 * 
	 * @author joseamontenegromontes
	 *
	 */
	class Values {
		double frecuencia;
		String code;
		int size;

		public Values(String codeA, double frecuenciaA) {
			frecuencia = frecuenciaA;
			code = codeA;
			size = codeA.length();
		}

		public String toString() {
			return frecuencia + " " + code;
		}
	}

}
