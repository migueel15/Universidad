/**
 * @author Pepe Gallardo, Data Structures, Grado en Informática. UMA.
 *
 * Hash Table with linear probing to resolve collisions
 */

package dataStructures.hashTable;

import java.util.Iterator;
import java.util.NoSuchElementException;

import dataStructures.tuple.Tuple2;

/**
 * Hash tables whose entries are associations of different keys to values
 * implemented using open addressing (linear probing). Note that keys should 
 * define {@link Object#hashCode} method properly.
 *
 * @param <K> Type of keys.
 * @param <V> Types of values.
 */public class LinearProbingHashTable<K,V> implements HashTable<K,V>{
	
	private K keys[];
	private V values[];
	private int size;
	private double maxLoadFactor;
	
	/**
	 * Creates an empty linear probing hash table.
	 * <p>Time complexity: O(1)
	 * @param numCells Initial number of cells in table (should be a prime number).
	 * @param loadFactor Maximum load factor to tolerate. If exceeded, rehashing is performed automatically.
	 */
	@SuppressWarnings("unchecked")
	public LinearProbingHashTable(int numCells, double loadFactor) {
		keys = (K[]) new Object[numCells];
		values = (V[]) new Object[numCells];
		size = 0;
		maxLoadFactor = loadFactor;		
	}
	
	/** 
	 * {@inheritDoc}
	 * <p>Time complexity: near O(1)
	 */
	public boolean isEmpty() {
		return size==0;
	}
	
	/** 
	 * {@inheritDoc}
	 * <p>Time complexity: near O(1)
	 */
	public int size() {
		return size;
	}
	
	private int hash(K key) {
		return (key.hashCode() & 0x7fffffff) % keys.length; 
	}
	
	private double loadFactor() {
		return (double)size / (double) keys.length;
	}
	
	private int searchIdx(K key) {
		int idx = hash(key);
		while((keys[idx] != null) && (!keys[idx].equals(key)))
			idx = (idx+1) % keys.length;
		return idx;	
	}	

	/** 
	 * {@inheritDoc}
	 * <p>Time complexity: near O(1)
	 */
	public V search(K key) {
		int idx = searchIdx(key);
		return values[idx];
	}
	
	/** 
	 * {@inheritDoc}
	 * <p>Time complexity: near O(1)
	 */
	public boolean isElem(K key) {
		return search(key) != null;
	}		
	
	/** 
	 * {@inheritDoc}
	 * <p>Time complexity: near O(1)
	 */
	public void insert(K key, V value) {
		if(loadFactor()>maxLoadFactor)
			rehashing();		
		
		int idx = searchIdx(key);
		if(keys[idx] == null) {
			keys[idx] = key;
			size++;
		}
		values[idx] = value;
	}
	
	/** 
	 * {@inheritDoc}
	 * <p>Time complexity: near O(1)
	 */
	public void delete(K key) {
		int idx = searchIdx(key);

		if(keys[idx] != null) { //found
		
			// delete item
			keys[idx] = null;
			values[idx] = null;
			size--;

			// rehash elems in same cluster
			idx = (idx+1) % keys.length;
			while(keys[idx] != null) {
				K k = keys[idx];
				V v = values[idx];
				
				keys[idx] = null;
				values[idx] = null;
				
				int newIdx = searchIdx(k);
				keys[newIdx] = k;
				values[newIdx] = v;
				
				idx = (idx+1) % keys.length;
			}
		}
  }
  
	@SuppressWarnings("unchecked")
  private void rehashing() {
		// compute new table size
		int newCapacity = HashPrimes.primeDoubleThan(keys.length);
		//System.out.printf("REHASH %d\n",newCapacity);		
		K oldKeys[] = keys;
		V oldValues[] = values;
		
		keys = (K[]) new Object[newCapacity];  	
		values = (V[]) new Object[newCapacity];
		
		// reinsert elements in new table
		for(int i=0; i<oldKeys.length; i++)
			if(oldKeys[i] != null) {
				int newIdx = searchIdx(oldKeys[i]);
				keys[newIdx] = oldKeys[i];
				values[newIdx] = oldValues[i];
			}
  	}

	// Almost an iterator
	private class TableIter {
		private int visited; // number of elements already visited by this iterator
 		protected int nextIdx; // index of next element to be visited by this iterator
  	
 		public TableIter() {
			visited = 0;
			nextIdx = -1; // so that after first increment it becomes 0
		}
  	
		public boolean hasNext() {
			return visited < size;
		}

		// advance nextIdx to be index of next to be visited element
		public void advance() {
			if (!hasNext())
				throw new NoSuchElementException();
			
			do {
				nextIdx = (nextIdx + 1) % keys.length;
			} while(keys[nextIdx] == null);
			
			visited++;
		}
	}	
  
	private class KeysIter extends TableIter implements Iterator<K> {
		public K next() {
			advance();
			return keys[nextIdx];
		}  	
	}

	private class ValuesIter extends TableIter implements Iterator<V> {
		public V next() {
			advance();
			return values[nextIdx];
		}  	
	}
  
	private class KeysValuesIter extends TableIter implements Iterator<Tuple2<K,V>> {
		public Tuple2<K,V> next() {
			advance();
			return new Tuple2<K,V>(keys[nextIdx],values[nextIdx]);
		}  	
	}
  
	public Iterable<K> keys() {
		return new Iterable<K>(){
			public Iterator<K> iterator() {
				return new KeysIter();
			}
		};
	}
	
	public Iterable<V> values() {
		return new Iterable<V>(){
			public Iterator<V> iterator() {
				return new ValuesIter();
			}
		};
	}
	
	public Iterable<Tuple2<K,V>> keysValues() {
		return new Iterable<Tuple2<K,V>>(){
			public Iterator<Tuple2<K,V>> iterator() {
				return new KeysValuesIter();
			}
		};
	}
	
	/** 
	 * Returns representation of hash table as a String.
	 */
	public String toString() {
    String className = getClass().getSimpleName();
		String s = className+"(";
			if(!isEmpty()) {
			for(Tuple2<K,V> t : keysValues())
				s += t._1()+"->"+t._2()+",";
			s = s.substring(0, s.length()-1);
		}
		s += ")";
	  return s;
	}	
  
}
