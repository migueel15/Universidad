package dataStructures.dictionary;
import dataStructures.tuple.Tuple2;

/**
 * Interface for bidirectional dictionaries (finite maps) associating keys to values.
 * @param <K1> Type of keys.
 * @param <K2> Type of values.
 */

public interface BiDictionary<K,V> {

		/**
		 * Test for dictionary emptiness.
		 * 
		 * @return {@code true} if dictionary is empty (stores zero key/value associations), else {@code false}.
		 */
		boolean isEmpty();

		/**
		 * Retrieves number of key/value associations in dictionary.
		 * @return Number of associations in dictionary.
		 */	
		int size();

		/**
		 * Inserts a new key/value association in dictionary. If key was already present
		 * in dictionary, old value is replaced by {@code v} (different associations for same key
		 * are not supported).
		 * @param k Key in association.
		 * @param v Value associated to key.
		 */
		void insert(K k, V v);
		
		
		/**
		 * Retrieves value associated to key {@code k}. If key is not
		 * in dictionary, {@code null} is returned.
		 * @param k Key for which associated value is sought.
		 * @return Value associated to key or {@code null} if key is not in dictionary. 
		 */
		V valueOf(K k);

		/**
		 * Retrieves key associated to value {@code v}. If value is not
		 * in dictionary, {@code null} is returned.
		 * @param v value for which associated value is sought.
		 * @return Key associated to value or {@code null} if value is not in dictionary. 
		 */
		K keyOf(V v);

		/**
		 * Tests whether an association with key {@code k} is included in dictionary.
		 * @param k Key of association.
		 * @return {@code true} if dictionary includes an association for key {@code k}, else {@code false}. 
		 */
		boolean isDefinedKeyAt(K k);

		/**
		 * Tests whether an association with value {@code v} is included in dictionary.
		 * @param v Value of association.
		 * @return {@code true} if dictionary includes an association for value {@code v}, else {@code false}. 
		 */
		boolean isDefinedValueAt(V v);

		/**
		 * Removes a key/value association from dictionary. If association with key {@code k} is not
		 * in dictionary, dictionary is not modified (this is not considered an
		 * error and thus no exception is thrown).
		 * @param k Key of association to remove.
		 */
		void deleteByKey(K k);

		/**
		 * Removes a key/value association from dictionary. If association with value {@code k} is not
		 * in dictionary, dictionary is not modified (this is not considered an
		 * error and thus no exception is thrown).
		 * @param v value of association to remove.
		 */
		void deleteByValue(V v);

		/** 
		 * Retrieves an {@code Iterable} over all keys in dictionary.
		 * Note that {@code remove} method is not supported in corresponding {@code iterator}. 
		 * Note also that dictionary structure or keys should not be modified during iteration as 
		 * iterator state may become inconsistent.
		 * @see java.lang.Iterable
		 * @return An {@code Iterable} over all keys in dictionary.
		 */
		Iterable<K> keys();

		/** 
		 * Retrieves an {@code Iterable} over all values in dictionary.
		 * Note that {@code remove} method is not supported in corresponding {@code iterator}. 
		 * Note also that dictionary structure or keys should not be modified during iteration as 
		 * iterator state may become inconsistent.
		 * @see java.lang.Iterable
		 * @return An {@code Iterable} over all keys in dictionary.
		 */
		Iterable<V> values();

		/** 
		 * Retrieves an {@code Iterable} over all keys and values in dictionary.
		 * Note that {@code remove} method is not supported in corresponding {@code iterator}. 
		 * Note also that dictionary structure or keys should not be modified during iteration as 
		 * iterator state may become inconsistent.
		 * @see java.lang.Iterable
		 * @return An {@code Iterable} over all keys in dictionary.
		 */
		Iterable<Tuple2<K,V>> keysValues();
	}
