public class RecorridoTablero {
	private int[][] tablero; 
	private int[][] solucion;
	private int fila; //Fila de la casilla de inicio
	private int col;  //Columna de la casilla de inicio
	private int n;   
	private int m;

	public RecorridoTablero(int[][] t, int fila, int col) {
		tablero = t;
		n = tablero.length;
		m = tablero[0].length;
		this.fila = fila;
		this.col = col;
		solucion = null;
	}


	public int resolverMemo() {
		// Creamos la tabla auxiliar
		solucion = new int[n][m]; // -1 representará que la celda está vacía.
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				solucion[i][j] = -1;
			}
		}
		// Rellenamos la tabla desde la casilla indicada
		return resuelve(fila, col);
	}

	/*
	private int resuelve(int i, int j) {
		if(i == n-1 || i == 0){
			solucion[i][j] = tablero[i][j];
		}
		if(i>0 && j>0 && j<m-1){
			int va = tablero[i][j];
			solucion[i][j] = maximo3(va+resuelve(i-1,j-1),va+resuelve(i-1,j),va+resuelve(i-1,j+1));
		}

		if(i>0 && j==0){
			int va = tablero[i][j];
			solucion[i][j] = maximo3(-1,va+resuelve(i-1,j),va+resuelve(i-1,j+1));
		}

		if(i>0 && j==m-1){
			int va = tablero[i][j];
			solucion[i][j] = maximo3(va+resuelve(i-1,j-1),va+resuelve(i-1,j),-1);
		}

		return solucion[i][j];
	}
	*/

	private int resuelve(int i, int j) {
		if(i == n-1 || i == 0){
			solucion[i][j] = tablero[i][j];
		}
		if(i<n-1 && j>0 && j<m-1){
			int va = tablero[i][j];
			solucion[i][j] = maximo3(va+resuelve(i+1,j-1),va+resuelve(i+1,j),va+resuelve(i+1,j+1));
		}

		if(i<n-1 && j==0){
			int va = tablero[i][j];
			solucion[i][j] = maximo3(-1,va+resuelve(i+1,j),va+resuelve(i+1,j+1));
		}

		if(i<n-1 && j==m-1){
			int va = tablero[i][j];
			solucion[i][j] = maximo3(va+resuelve(i+1,j-1),va+resuelve(i+1,j),-1);
		}

		return solucion[i][j];
	}

	private int maximo3(int a, int b, int c) {
		int res = a;
		if (b > res) {
			res = b;
		}
		if (c > res) {
			res = c;
		}
		return res;
	}

	public Recorrido reconstruirSol() {
		if (solucion == null) {
			throw new RuntimeException("Se debe resolver el problema primero");
		}
		Recorrido r = new Recorrido();
		int anti = -1;
		int antj = -1;
		// BASE
		for(int j = 0; j < m; j++){
			if(solucion[0][j] != -1){
				r.add(0,j);
				anti = 0;
				antj = j;
			}
		}
		for(int i = 1; i <= n-1; i++){
			boolean encontrado =false;
			for(int j = antj-1; j <= antj+1; j++){
				try {
					if(solucion[i][j] == solucion[anti][antj] - tablero[anti][antj] && !encontrado){
						encontrado = true;
						anti = i;
						antj = j;
						r.add(i,j);
					}
				}catch (IndexOutOfBoundsException e){/*pasa a la siguiente fila*/};
			}
		}
		return r;
	}

	public void imprimeMatrizSolucion() {
		for (int i = 0; i < solucion.length; i++) {
			for (int j = 0; j < solucion[i].length; j++) {
				System.out.print(solucion[i][j] + " ");
			}
			System.out.println(" ");
		}
	}
}
