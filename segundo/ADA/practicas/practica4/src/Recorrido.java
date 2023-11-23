import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Recorrido {
	private List<String> camino;
	
	public Recorrido() {
		camino = new LinkedList<String>();
	}
		
	public void add(int i, int j) {
		camino.add("(" + i + "," + j + ")");
	}
	
	@Override 
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if (camino.size()> 0) {
			Iterator<String> it = camino.iterator();
			sb.append(it.next());
			while (it.hasNext()) {
				sb.append(" -> ");
				sb.append(it.next());
			}	
		}	
		return sb.toString();
	}
}
