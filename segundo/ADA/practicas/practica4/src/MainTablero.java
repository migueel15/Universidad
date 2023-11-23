public class MainTablero {
	
	private static int[][] costes= {{2,7,11,12,4},{14,0,3,9,0},{4,7,13,4,3},{7,5,19,4,3},{3,10,5,13,11}};

	public static void main(String[] args) {
		RecorridoTablero p=new RecorridoTablero(costes,4,1);
		int res =p.resolverMemo();
		System.out.println("La solución es " + res);
		Recorrido camino= p.reconstruirSol();	
		System.out.println(camino);
		p.imprimeMatrizSolucion();

	}



}
