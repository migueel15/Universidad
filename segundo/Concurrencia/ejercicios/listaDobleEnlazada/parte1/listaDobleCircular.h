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

// Comprueba si la lista está vacía
// Devuelve 1 si esta vacía y cero si no lo está
int estaVacia(TListaDoble l);

// Crea una lista vacía
void crear(TListaDoble *l);

/*Crea una lista leyendo los datos desde un fichero de texto
 *  - El fichero se proporciona junto al esqueleto
 *  - El formato del fichero es el siguiente, donde el nombre de cada persona
 * está en una línea distinta: Maria Juan Antonio Alberto
 */
void crearDesdeFicheroTexto(char *nomFic, TListaDoble *l);

/*Crear una lista desde un fichero de binario
 *   - El fichero se proporciona junto al esqueleto
 *   - El nombre de cada persona se escribe de forma consecutiva en el fichero
 *     incluyendo tambien el fin de cadena ('\0'). Ese fin de cadena permite
 *     diferenciar el nombre de una persona del de la siguiente en el fichero
 *
 *   - Se recomienda leer los datos del fichero caracter a caracter, e
 * implementar de forma privada al módulo la siguiente función:
 *
 *        void leerNombre(char *persona,FILE *fd);
 *
 *     Esta función leerá del fichero caracter a caracter hasta encontrar el
 * caracter
 *     '\0' y almacenará todos esos caracteres (incluyendo '\0' en el parámetro
 * persona
 */
void crearDesdeFicheroBinario(char *nomFic, TListaDoble *l);

/*Insertar al principio de una lista doblemente enlazada y circular
 *
 * Hay que tener en cuenta los siguientes casos:
 *   - Que la lista esté vacía (el puntero sig y ant del nuevo nodo apuntan a sí
 * mismo)
 *   - Que la lista tenga un solo elemento
 *   - Que la lista tenga más de un elemento
 */
void insertar(TListaDoble *l, char *persona);

/* Mostrar la lista en orden de inserción
 *   Se mostrará primero el elemento que primero se insertó
 *   Tened en cuenta que al insertar cada nodo nuevo se inserta
 *   al principio de la lista
 */
void mostrar(TListaDoble l);

// Mostrar la lista en orden inverso al de inserción
void mostrarInverso(TListaDoble l);

/* Mostrar la lista en un fichero de texto.
 *   Se puede mostrar en el orden de inserción o en el orden inverso.
 *   En la solución que luego se proporcionará se muestran en el orden
 *   de inserción
 */
void mostrarEnFicheroTexto(char *nomFic, TListaDoble l);

/* Mostrar la lista en un fichero binario
 *   Se puede mostrar en el orden de inserción o en el orden inverso.
 *   En la solución que luego se proporcionará se muestran en el orden
 *   de inserción
 */
void mostrarEnFicheroBinario(char *nomFic, TListaDoble l);

/*Borrar del principio de una lista
 * Hay que tener en cuenta los siguientes casos:
 *   - Que la lista esté vacía (no se hace nada)
 *   - Que la lista tenga un solo elemento
 *   - Que la lista tenga más de un elemento
 */
void borrarPrincipio(TListaDoble *l);

/* Borrar el nodo que contiene a la persona indicada como segundo parámetro
 *  En este borrar y también en el anterior hay que tener en cuenta
 *  que podemos navegar al nodo anterior y posterior al nodo que queremos
 *  borrar con los punteros ant y sig, por lo que no hacen falta todos los
 *  punteros auxiliares que utilizamos al borrar de una lista simple
 *
 *  Por ejemplo:
 *    - Podemos hacer ptr->ant->sig = ptr->sig; para acceder al nodo anterior
 *      sin que nos haga falta el puntero "ant" auxiliar que utilizábamos con
 *      las listas simples.
 */
void borrarNodo(TListaDoble *l, char *persona);

/*Devuelve la persona almacenada en el nodo que está a distancia dist
 *  del nodo nodoIni (teniendo en cuenta que la lista es circular)
 *  Si sentido es 1 nos desplazamos en el sentido de las agujas del reloj
 *  Si sentido es 2 nos desplazamos en el sentido contrario al de las agujas del
 * reloj
 *
 *  Los pasos básicos serían:
 *    - Primero nos colocamos en el nodo indicado en nodoIni, empezando a contar
 * desde el primer elemento de la lista (que sería el nodo 0)
 *    - Después nos movemos a la derecha o a la izquierda en la lista, de forma
 * circular, tantos nodos como indica el parámetro dist.
 *    - Por último, guardamos los datos de ese nodo en persona
 */
void datosDistanciaD(TListaDoble l, int dist, int sentido, int nodoIni,
                     char *persona);

// Devuelve el numero de elementos en la lista
int longitud(TListaDoble l);

// Destruye la lista, inicializándola nuevamente a NULL
void destruir(TListaDoble *l);

#endif /* LISTADOBLECIRCULAR_H_ */
