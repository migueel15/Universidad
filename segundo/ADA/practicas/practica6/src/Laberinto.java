import java.util.*;

public class Laberinto {
	private int[][] laberinto;
	private Posicion entrada, salida;
	private int n; // número de filas y columnas

	public Laberinto(int[][] lab, Posicion ent, Posicion sal) {
		this.laberinto = lab;
		this.entrada = ent;
		this.salida = sal;
		this.n = lab.length;
	}

	public int getNumFilas() {
		return n;
	}

	public int getNumCols() {
		return n;
	}
	
	public int [][] getLaberinto(){
		return laberinto;
	}
	public Posicion getEntrada() {
		return entrada;
	}
	public Posicion getSalida() {
		return salida;
	}
	
	public List<Posicion> encontrarCamino() {
		List<Posicion> lista = new ArrayList<Posicion>();
		lista.add(entrada);
		return (encontrarCamino(lista)) ? lista : null;
	}

	/**
	 * Algoritmo de Vuelta Atrás para encontrar un camino que nos permita
	 * salir del laberinto
	 */
	private boolean encontrarCamino(List<Posicion> sol) {
		if(esCompleta(sol)){
			return true;
		}else{
			boolean haySol = false;
			ArrayList<Posicion> candidatos =
					posicionesCandidatas(sol.get(sol.size()-1),sol);

			int idxCandidatos = 0;
			while (!haySol && idxCandidatos < candidatos.size()){
				sol.add(candidatos.get(idxCandidatos));
				haySol = encontrarCamino(sol);

				if(!haySol){
					sol.remove(sol.size()-1);
				}

				idxCandidatos++;
			}
			return haySol;
		}
	}

	private ArrayList<Posicion> posicionesCandidatas(Posicion posActual,
																									 List<Posicion> sol){
		ArrayList<Posicion> candidatos = new ArrayList<>();
		if(valida(siguiente(posActual,1),sol)){
			candidatos.add(siguiente(posActual,1));
		}
		if(valida(siguiente(posActual,2),sol)){
			candidatos.add(siguiente(posActual,2));
		}
		if(valida(siguiente(posActual,3),sol)){
			candidatos.add(siguiente(posActual,3));
		}
		if(valida(siguiente(posActual,4),sol)){
			candidatos.add(siguiente(posActual,4));
		}

		return candidatos;
	}

	/**
	 * Comprueba si una solución es completa.
	 */
	private boolean esCompleta(List<Posicion> sol) {
		//TODO
		boolean esCompleta = true;
		if(!sol.contains(entrada) || !sol.contains(salida)){
			esCompleta = false;
		}
		return esCompleta;
	}

	/**
	 * Comprueba que la posición dada es una candidata válida para el siguiente paso
	 */
	private boolean valida(Posicion candidata, List<Posicion> sol) {
		//***Completar la implementación****
		return !sol.contains(candidata) && !esMuro(candidata) && estaEnLaberinto(candidata);
	}

	/**
	 * Devuelve true si en la posición p hay un muro
	 */
	private boolean esMuro(Posicion p) {
		//***Completar la implementación****
		if(estaEnLaberinto(p)){
			return laberinto[p.getX()][p.getY()] == -1;
		}else return false;
	}

	/**
	 * Devuelve true si la posición dada está dentro del laberinto.
	 */
	private boolean estaEnLaberinto(Posicion pos) {
		//***Completar la implementación****
		return pos.getX() >= 0 && pos.getX() < getNumFilas() && pos.getY() >= 0 && pos.getY() < getNumCols();
	}

	/**
	 * Dada una posición cartesiana devuelve la siguiente posición en el sentido
	 * indicado. Precondición: actual != null
	 * 
	 * @param actual Posición de partida
	 * @param dir    Sentido en el que hay que desplazarse (1->Norte, 2->Sur,
	 *               3->Este, 4-> Oeste)
	 * @return La nueva posición.
	 */
	private Posicion siguiente(Posicion actual, int dir) {
		int x = actual.getX();
		int y = actual.getY();
		if (dir == 1) {
			x--;
		} else if (dir == 2) {
			x++;
		} else if (dir == 3) {
			y++;
		} else {
			y--;
		}
		return new Posicion(x, y);
	}

	/**
	 * Devuelve todos los caminos para salir del laberinto.
	 */
	public List<List<Posicion>> encontrarCaminos() {
		List<List<Posicion>> todosCaminos = new ArrayList<List<Posicion>>();
		List<Posicion> sol = new ArrayList<Posicion>();
		sol.add(entrada);
		encontrarCaminos(sol, todosCaminos);
		return todosCaminos;
	}

	/**
	 * Algoritmo de Vuelta Atrás para encontrar todas las soluciones
	 */
	private void encontrarCaminos(List<Posicion> sol, List<List<Posicion>> todas) {
		if(esCompleta(sol)){
			todas.add(sol);
		}else{
			ArrayList<Posicion> candidatos =
					posicionesCandidatas(sol.get(sol.size()-1),sol);

			if(!candidatos.isEmpty()){
				for(Posicion pos : candidatos){
					ArrayList<Posicion> aux = new ArrayList<>(sol);
					aux.add(pos);
					encontrarCaminos(aux,todas);
				}
			}
		}
	}

	public List<Posicion> encontrarCaminoMasCortoVA() {
		List<Posicion> sol = new ArrayList<Posicion>();
		sol.add(entrada);
		return encontrarCaminoMasCortoVA(sol, null);
	}

	/**
	 * Algoritmo de Vuelta Atrás que devuelve la mejor solución encontrada
	 */
	private List<Posicion> encontrarCaminoMasCortoVA(List<Posicion> sol, List<Posicion> mejor) {

		//***Completar la implementación****
		List<List<Posicion>> todas = new ArrayList<>();
		encontrarCaminos(sol,todas);
		for(List<Posicion> actual : todas){
			if(calidad(actual) < calidad(mejor)){
				mejor = actual;
			}
		}
		return mejor;
	}

	/**
	 * Devuelve la calidad de la solución indicada
	 */
	private int calidad(List<Posicion> sol) {
		//***Completar la implementación****
		if(sol == null){
			return Integer.MAX_VALUE;
		}else return sol.size();
	}

	public List<Posicion> encontrarCaminoMasCortoBB() {
		ColaPrioridad c = new ColaPrioridad();// Creamos la estructura de datos

		List<Posicion> solInicial = new ArrayList<>();
		solInicial.add(entrada);
		Estado inicial = new Estado(solInicial, funcionCota(solInicial)); // Creamos el estado inicial
		c.insertar(inicial);

		List<Posicion> mejor = null;	//
		int cotaMejor = Integer.MAX_VALUE; // infinito

		while(!c.estaVacia()){
			Estado act = c.extraer();
			if(esCompleta(act.getCamino())){
				if(act.cota() < cotaMejor){
					cotaMejor = act.cota();
					mejor = act.getCamino();
					c.eliminar(act.cota());
				}
			}else{
				List<Posicion> hijosCandidatos =
						posicionesCandidatas(act.getCamino().get(act.getCamino().size()-1),act.getCamino());
				for (Posicion hijo : hijosCandidatos){
					List<Posicion> aux = new ArrayList<>(act.getCamino());
					aux.add(hijo);
					if(funcionCota(aux) < cotaMejor){
						c.insertar(new Estado(aux,funcionCota(aux)));
					}
				}
			}

		}

		return mejor;
	}

	/**
	 *  Devuelve el valor de cota de la solución indicada. 
	 */
	private int funcionCota(List<Posicion> sol) {
		return Math.abs(salida.getX()-sol.get(sol.size()-1).getX()) +
		Math.abs(salida.getY()-sol.get(sol.size()-1).getY()) + sol.size()-1;
	}
}
