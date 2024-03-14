/*
 * arbolbb.h
 *
 *  Created on: 4/4/2016
 *      Author: esc
 */

#ifndef ARBOLBB_H_
#define ARBOLBB_H_
#include <stdio.h>

typedef struct T_Nodo* T_Arbol;

	struct T_Nodo {
			unsigned dato;
			T_Arbol izq, der;
		};

// Crea la estructura utilizada para gestionar la memoria disponible.
	void Crear(T_Arbol* arbol);

// Destruye la estructura utilizada.
	void Destruir(T_Arbol* arbol);

	// Inserta num en el arbol
	void Insertar(T_Arbol* arbol,unsigned num);
	// Muestra el contenido del árbol en InOrden
	void Mostrar(T_Arbol arbol);
	// Guarda en disco el contenido del arbol
	void Salvar(T_Arbol arbol, FILE* fichero);

#endif /* ARBOLBB_H_ */
