/**
 * @author Pepe Gallardo, Data Structures, Grado en Informática. UMA.
 *
 * Left case for union type
 */

package dataStructures.either;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.StringJoiner;

public class Left<A,B> implements Either<A,B> {
	private final A left;
	
	/**
	 * Creates a left value storing {@code left}
	 * @param left Value to store in {@code Either object}.
	 */
	public Left(A left) { this.left = left; }
	
	public boolean isLeft() { return true; }
	public boolean isRight() { return false; }
	public A left() { return left; }
	public B right() { throw new NoSuchElementException("right on Left object"); }
	
	/** 
	 * Returns representation of {@code Either} object as a String.
	 */
	@Override
	public String toString() {
		String className = getClass().getSimpleName();
		StringJoiner sj = new StringJoiner(", ", className+"(", ")");
		sj.add(left.toString());
		return sj.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Left<?, ?> that = (Left<?, ?>) o;
		return Objects.equals(left, that.left);
	}

	@Override
	public int hashCode() {
		return Objects.hash(left);
	}
}
