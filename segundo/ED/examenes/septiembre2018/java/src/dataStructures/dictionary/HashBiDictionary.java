package dataStructures.dictionary;
import dataStructures.list.List;

import dataStructures.list.ArrayList;
import dataStructures.set.AVLSet;
import dataStructures.set.Set;
import dataStructures.tuple.Tuple2;

/**
 * Estructuras de Datos. Grados en Informatica. UMA.
 * Examen de septiembre de 2018.
 *
 * Apellidos, Nombre:
 * Titulacion, Grupo:
 */
public class HashBiDictionary<K,V> implements BiDictionary<K,V>{
	private Dictionary<K,V> bKeys;
	private Dictionary<V,K> bValues;
	
	public HashBiDictionary() {
    bKeys = new HashDictionary<K,V>();
    bValues = new HashDictionary<V,K>();
	}
	
	public boolean isEmpty() {
    return bKeys.isEmpty() && bValues.isEmpty();
	}
	
	public int size() {
    return bKeys.size();
	}
	
	public void insert(K k, V v) {
    bKeys.insert(k, v);
    bValues.insert(v,k);
	}
	
	public V valueOf(K k) {
    return bKeys.isDefinedAt(k) ? bKeys.valueOf(k) : null;
	}
	
	public K keyOf(V v) {
    return bValues.isDefinedAt(v) ? bValues.valueOf(v) : null;
	}
	
	public boolean isDefinedKeyAt(K k) {
		return bKeys.isDefinedAt(k);
	}
	
	public boolean isDefinedValueAt(V v) {
		return bValues.isDefinedAt(v);
	}
	
	public void deleteByKey(K k) {
    if(isDefinedKeyAt(k)){
      V value = bKeys.valueOf(k);
      if(isDefinedValueAt(value)){
        bValues.delete(value);
      }
      bKeys.delete(k);
    }
	}
	
	public void deleteByValue(V v) {
    if(isDefinedValueAt(v)){
      K key = bValues.valueOf(v);
      if(isDefinedKeyAt(key)){
        bKeys.delete(key);
      }
      bValues.delete(v);
    }
	}
	
	public Iterable<K> keys() {
		return bKeys.keys();
	}
	
	public Iterable<V> values() {
		return bValues.keys();
	}
	
	public Iterable<Tuple2<K, V>> keysValues() {
		return bKeys.keysValues();
	}
	
		
	public static <K,V extends Comparable<? super V>> BiDictionary<K, V> toBiDictionary(Dictionary<K,V> dict) {
    BiDictionary<K,V> biDictionary = new HashBiDictionary<>();
    // TODO check size for runtime error
    
    for(Tuple2<K,V> tupla : dict.keysValues()){
      biDictionary.insert(tupla._1(), tupla._2());
    }
    return biDictionary;
	}
	
	public <W> BiDictionary<K, W> compose(BiDictionary<V,W> bdic) {
    BiDictionary<K,W> biDictionary = new HashBiDictionary<>();
    for(Tuple2<K,V> c_tupla : bKeys.keysValues()){
      for(Tuple2<V,W> new_tupla : bdic.keysValues()){
        if(c_tupla._2().equals(new_tupla._1())){
          biDictionary.insert(c_tupla._1(), new_tupla._2());
        }
      }
    }
    return biDictionary;
	}
		
	public static <K extends Comparable<? super K>> boolean isPermutation(BiDictionary<K,K> bd) {
    boolean perm = true;
    List<K> keys = new ArrayList<>();
    List<K> values = new ArrayList<>();

    for(Tuple2<K,K> current : bd.keysValues()){
      keys.append(current._1());
      values.append(current._2());
    }

    if(keys.size() == values.size()){
      while(!keys.isEmpty() && !values.isEmpty() && perm){
        K k = keys.get(keys.size()-1);
        keys.remove(keys.size()-1);
        boolean exist = false;
        for(int i = 0; i<values.size() && !exist; i++){
          if(k.equals(values.get(i))){
            exist = true;
          }
        }

        if(!exist){
          perm = false;
        }
      }
    }else{
      perm = false;
    }
    return perm;
	}
	
	// Solo alumnos con evaluacion por examen final.
    // =====================================
	
	public static <K extends Comparable<? super K>> List<K> orbitOf(K k, BiDictionary<K,K> bd) {
		// TODO
		return null;
	}
	
	public static <K extends Comparable<? super K>> List<List<K>> cyclesOf(BiDictionary<K,K> bd) {
		// TODO
		return null;
	}

    // =====================================
	
	
	@Override
	public String toString() {
		return "HashBiDictionary [bKeys=" + bKeys + ", bValues=" + bValues + "]";
	}
	
	
}
