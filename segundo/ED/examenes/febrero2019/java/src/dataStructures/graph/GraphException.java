/**
 * @author Data Structures. ETSI Informática. UMA.
 *
 * Exceptions for graphs
 */

package dataStructures.graph;

public class GraphException extends RuntimeException {

	private static final long serialVersionUID = -7807923404308749451L;

	public GraphException() { }

	public GraphException(String msg) {
		super(msg);
	}
}
