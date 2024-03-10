/*
 * listaDobleCircular.h
 *
 *  Created on: 19 mar. 2021
 *      Author: Monica
 */

#ifndef LISTADOBLECIRCULAR_H_
#define LISTADOBLECIRCULAR_H_

typedef struct TNodo *TListaDoble;
struct TNodo {
  char persona[20];
  TListaDoble ant, sig;
};

// Comprueba si la lista est� vac�a
// Devuelve 1 si esta vac�a y cero si no lo est�
int estaVacia(TListaDoble l);

// Crea una lista vac�a
void crear(TListaDoble *l);

/*Crea una lista leyendo los datos desde un fichero de texto
 *  - El fichero se proporciona junto al esqueleto
 *  - El formato del fichero es el siguiente, donde el nombre de cada persona
 * est� en una l�nea distinta: Maria Juan Antonio Alberto
 */
void crearDesdeFicheroTexto(char *nomFic, TListaDoble *l);

/*Crear una lista desde un fichero de binario
 *   - El fichero se proporciona junto al esqueleto
 *   - El nombre de cada persona se escribe de forma consecutiva en el fichero
 *     incluyendo tambien el fin de cadena ('\0'). Ese fin de cadena permite
 *     diferenciar el nombre de una persona del de la siguiente en el fichero
 *
 *   - Se recomienda leer los datos del fichero caracter a caracter, e
 * implementar de forma privada al m�dulo la siguiente funci�n:
 *
 *        void leerNombre(char *persona,FILE *fd);
 *
 *     Esta funci�n leer� del fichero caracter a caracter hasta encontrar el
 * caracter
 *     '\0' y almacenar� todos esos caracteres (incluyendo '\0' en el par�metro
 * persona
 */
void crearDesdeFicheroBinario(char *nomFic, TListaDoble *l);

/*Insertar al principio de una lista doblemente enlazada y circular
 *
 * Hay que tener en cuenta los siguientes casos:
 *   - Que la lista est� vac�a (el puntero sig y ant del nuevo nodo apuntan a s�
 * mismo)
 *   - Que la lista tenga un solo elemento
 *   - Que la lista tenga m�s de un elemento
 */
void insertar(TListaDoble *l, char *persona);

/* Mostrar la lista en orden de inserci�n
 *   Se mostrar� primero el elemento que primero se insert�
 *   Tened en cuenta que al insertar cada nodo nuevo se inserta
 *   al principio de la lista
 */
void mostrar(TListaDoble l);

// Mostrar la lista en orden inverso al de inserci�n
void mostrarInverso(TListaDoble l);

/* Mostrar la lista en un fichero de texto.
 *   Se puede mostrar en el orden de inserci�n o en el orden inverso.
 *   En la soluci�n que luego se proporcionar� se muestran en el orden
 *   de inserci�n
 */
void mostrarEnFicheroTexto(char *nomFic, TListaDoble l);

/* Mostrar la lista en un fichero binario
 *   Se puede mostrar en el orden de inserci�n o en el orden inverso.
 *   En la soluci�n que luego se proporcionar� se muestran en el orden
 *   de inserci�n
 */
void mostrarEnFicheroBinario(char *nomFic, TListaDoble l);

/*Borrar del principio de una lista
 * Hay que tener en cuenta los siguientes casos:
 *   - Que la lista est� vac�a (no se hace nada)
 *   - Que la lista tenga un solo elemento
 *   - Que la lista tenga m�s de un elemento
 */
void borrarPrincipio(TListaDoble *l);

/* Borrar el nodo que contiene a la persona indicada como segundo par�metro
 *  En este borrar y tambi�n en el anterior hay que tener en cuenta
 *  que podemos navegar al nodo anterior y posterior al nodo que queremos
 *  borrar con los punteros ant y sig, por lo que no hacen falta todos los
 *  punteros auxiliares que utilizamos al borrar de una lista simple
 *
 *  Por ejemplo:
 *    - Podemos hacer ptr->ant->sig = ptr->sig; para acceder al nodo anterior
 *      sin que nos haga falta el puntero "ant" auxiliar que utiliz�bamos con
 *      las listas simples.
 */
void borrarNodo(TListaDoble *l, char *persona);

/*Devuelve la persona almacenada en el nodo que est� a distancia dist
 *  del nodo nodoIni (teniendo en cuenta que la lista es circular)
 *  Si sentido es 1 nos desplazamos en el sentido de las agujas del reloj
 *  Si sentido es 2 nos desplazamos en el sentido contrario al de las agujas del
 * reloj
 *
 *  Los pasos b�sicos ser�an:
 *    - Primero nos colocamos en el nodo indicado en nodoIni, empezando a contar
 * desde el primer elemento de la lista (que ser�a el nodo 0)
 *    - Despu�s nos movemos a la derecha o a la izquierda en la lista, de forma
 * circular, tantos nodos como indica el par�metro dist.
 *    - Por �ltimo, guardamos los datos de ese nodo en persona
 */
void datosDistanciaD(TListaDoble l, int dist, int sentido, int nodoIni,
                     char *persona);

// Devuelve el numero de elementos en la lista
int longitud(TListaDoble l);

// Destruye la lista, inicializ�ndola nuevamente a NULL
void destruir(TListaDoble *l);

#endif /* LISTADOBLECIRCULAR_H_ */
