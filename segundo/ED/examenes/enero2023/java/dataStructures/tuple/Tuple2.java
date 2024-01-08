/**
 * @author Pepe Gallardo, Data Structures, Grado en Informática. UMA.
 *
 * 2-component tuple type
 */

package dataStructures.tuple;

import java.util.Objects;
import java.util.StringJoiner;

/**
 * Tuples with two components.
 *
 * @param <A> Type of first component.
 * @param <B> Type of second component.
 */
public class Tuple2<A,B> {
	private final A _1;
	private final B _2;
	
	/**
	 * Creates a tuple with two components.
	 * @param _1 First component.
	 * @param _2 Second component.
	 */
	public Tuple2(A _1, B _2) {
		this._1 = _1;
		this._2 = _2;
	}
	
	/**
	 * Retrieves first component of tuple.
	 * @return First component of tuple.
	 */
	public A _1() {
		return _1;
	}
	
	/**
	 * Retrieves second component of tuple.
	 * @return Second component of tuple.
	 */
	public B _2() {
		return _2;
	}
	
	/** 
	 * Returns representation of tuple as a String.
	 */
	@Override
	public String toString() {
		String className = getClass().getSimpleName();
		StringJoiner sj = new StringJoiner(", ", className+"(", ")");
		sj.add(_1.toString());
		sj.add(_2.toString());
		return sj.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Tuple2<?, ?> that = (Tuple2<?, ?>) o;
		return Objects.equals(_1, that._1) && Objects.equals(_2, that._2);
	}

	@Override
	public int hashCode() {
		int result = 1;
		result = 31 * result + Objects.hash(_1);
		result = 31 * result + Objects.hash(_2);
		return result;
	}
}
