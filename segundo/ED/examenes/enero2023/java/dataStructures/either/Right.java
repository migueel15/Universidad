/**
 * @author Pepe Gallardo, Data Structures, Grado en Informática. UMA.
 *
 * Right case for union type
 */
 
package dataStructures.either;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.StringJoiner;

public class Right<A,B> implements Either<A,B> {
	private final B right;
	
	/**
	 * Creates a right value storing {@code right}
	 * @param right Value to store in {@code Either object}.
	 */
	public Right(B right) { this.right = right; }

	public boolean isLeft() { return false; }
	public boolean isRight() { return true; }
	public A left() { throw new NoSuchElementException("left on Right object"); }
	public B right() { return right; }
	
	/** 
	 * Returns representation of {@code Either} object as a String.
	 */
	@Override
	public String toString() {
		String className = getClass().getSimpleName();
		StringJoiner sj = new StringJoiner(", ", className+"(", ")");
		sj.add(right.toString());
		return sj.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Right<?, ?> that = (Right<?, ?>) o;
		return Objects.equals(right, that.right);
	}

	@Override
	public int hashCode() {
		return Objects.hash(right);
	}
}
