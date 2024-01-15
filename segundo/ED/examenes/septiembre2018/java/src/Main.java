import dataStructures.dictionary.HashBiDictionary;
import dataStructures.dictionary.HashDictionary;
import dataStructures.list.List;
import dataStructures.dictionary.BiDictionary;
import dataStructures.dictionary.Dictionary;

public class Main {
    public static void main(String[] args) {
    	// Completa la clase dataStructures.dictionary/HashBDictionary.java
    	BiDictionary<String, Integer> bdict = new HashBiDictionary<>();
        
    	// apartados a y b
        System.out.println("Inicialmente isEmpty es " + bdict.isEmpty());
        System.out.println();
        
        String[] palabras = { "one", "three", "five", "ten" };
        int[] numeros = { 1, 3, 5, 10 };
        for (int i=0; i < palabras.length; i++) {
        	bdict.insert(palabras[i], numeros[i]);
        }
        System.out.println(bdict);
        System.out.println("isEmpty es ahora " + bdict.isEmpty());
        System.out.println("Size " + bdict.size());
        System.out.println();
        
        // apartados c y d
        System.out.println("Valor asociado a one: " + bdict.valueOf("one"));
        System.out.println("Clave asociada a 1: " + bdict.keyOf(1));
        System.out.println();
        
        // apartado e
        System.out.println("Se borra la asociación con clave \"one\"");
        bdict.deleteByKey("one");
        System.out.println(bdict);
        System.out.println();
        
        // apartado f
        System.out.println("Se borra la asociación con valor 3");
        bdict.deleteByValue(3);
        System.out.println(bdict);
        System.out.println();

        // apartado g
        Dictionary<String, Integer> dict = new HashDictionary<>();
        for (int i=0; i < palabras.length; i++) {
            dict.insert(palabras[i], numeros[i]);
        }
        BiDictionary<String, Integer> bdictTest = HashBiDictionary.toBDictionary(dict);
        System.out.println("A partir de " + dict + " se genera " + bdictTest);
        System.out.println();
        
        // Ahora no es inyectivo
        dict.insert("threeduplicate", 3);
        try {
        	@SuppressWarnings("unused")
			BiDictionary<String, Integer> bdictTest2 = HashBiDictionary.toBDictionary(dict);
        }catch( IllegalArgumentException e) {
        	System.out.println("Debe entrar por aqui ya que el diccionario no es inyectivo");
        	System.out.println();
        }
        
        // apartado h
        int[] k1 = { 1, 4, 2, 7 };
        char[] v1= { 'a', 'd', 'b', 'c' };
        String[] w1= { "l1", "l2", "l3", "l4" };
        HashBiDictionary<Integer, Character> bdict1 = new HashBiDictionary<>();
        HashBiDictionary<Character, String> bdict2 = new HashBiDictionary<>();
        for( int i=0; i < k1.length; i++) {
        	bdict1.insert(k1[i], v1[i]);
        	bdict2.insert(v1[i], w1[i]);
        }
        System.out.println(bdict1);
        System.out.println(bdict2);
        System.out.println("Componer bdict1 y bdict2: " + bdict1.compose(bdict2));
        System.out.println();
        
        int[] numeros1 = { 1, 4, 2, 7, 5, 3 };
        int[] numeros2 = { 4, 5, 3, 1, 7, 2 };
        
        // apartado i
        BiDictionary<Integer, Integer> permutationDict = new HashBiDictionary<>();
        for( int i=0; i < numeros1.length; i++) {
        	permutationDict.insert(numeros1[i], numeros2[i]);
        }
        System.out.println(permutationDict);
        System.out.println("Is permutation " + HashBiDictionary.isPermutation(permutationDict));
        System.out.println();
        
        
        // Solo alumnos con evaluación por examen final.
        // =====================================
        // Quitar comentarios a las lineas siguientes
        // para probar orbitOf y cyclesOf

        // debe dar [1,4,5,7]
        // List<Integer> orbit = HashBDictionary.orbitOf(1, permutationDict);
        // System.out.println("Órbita de 1 " + orbit);
        
        // debe dar [ [1,4,5,7], [2,3] ] (el orden puede variar)
        // List<List<Integer>> cycles = HashBDictionary.cyclesOf(permutationDict);
        // System.out.println("Ciclos " + cycles);
                
    }
}
