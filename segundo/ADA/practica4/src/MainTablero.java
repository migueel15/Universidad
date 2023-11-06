public class MainTablero {
	
	private static int[][] costes= {{1,4,8,3},{1,5,2,10},{8,2,7,9},{3,5,2,1}};

	public static void main(String[] args) {
		RecorridoTablero p=new RecorridoTablero(costes,3,1);
		int res =p.resolverMemo();
		System.out.println("La solución es " + res);
		Recorrido camino= p.reconstruirSol();	
		System.out.println(camino);
		p.imprimeMatrizSolucion();
		
	}

}
