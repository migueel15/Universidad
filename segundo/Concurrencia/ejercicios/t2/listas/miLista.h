#ifndef __MILISTA_H__
#define __MILISTA_H__

#include <stdio.h>  //Entrada-salida
#include <stdlib.h> //Memoria dinamica

// Definición tipos para lista enlazada
typedef struct TNode *TLista;
struct TNode {
  int valor;
  TLista next;
};

// Cabecera de las funciones para trabajar con listas enlazadas

// Operaciones de creacion de la lista

// Operaciones para insertar elementos en la lista
void insertarAlPrincipio(TLista *ptr, int valor);
// Operaciones para borrar elementos de la lista

// Operaciones de busqueda sobre la lista

// Operaciones de liberacion de la memoria dinamica de la lista

#endif
