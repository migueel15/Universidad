/*
 * Incidencias.h
 */

#ifndef INCIDENCIAS_H_
#define INCIDENCIAS_H_

typedef struct Incidencia *ListaIncidencias;
typedef struct Incidencia
{
	int id;						// Id único para cada incidencia.
	char descripcion[50];		// Descripción de la incidencia.
	int puntuacion;				// Valor entre 0..100. -1 si está sin evaluar.
	ListaIncidencias siguiente; // Puntero a la siguiente incidencia.
} Incidencia;

// Inicializa el array de incidencias ya creado en el main (ListaIncidencias *array) con sus elementos a nulo. El array tiene tamaño t.
// Inicializa la variable contId a cero.
// 0.25 pts.
void inicializarListaIncidencias(ListaIncidencias *array, int t);

// Se inserta una nueva incidencia con la prioridad y descripción dada. Asumimos que no van a insertar una duplicada, y que el array tiene longitud para almacenar la prioridad dada. Esta función devuelve el id de la incidencia generada. Recuerda que el array ya esta creado en el main.
// 1.75 pts.
int insertarIncidencia(ListaIncidencias *array, int prioridad, char *descripcion);

// Muestra las incidencias por orden de prioridad, primero las más prioritarias. Se debe mostrar la prioridad, su descripción, y su evaluación.
// [Prioridad0 – id0] Descripción1 Sin Evaluar
// [Prioridad0 – id1] Descripción2 Evaluada: 3
// [Prioridad1 – id2] Descripción3 Evaluada: 4
// t es el tamaño del array.
// 1.0 pt.
void mostrarIncidencias(ListaIncidencias *array, int t);

// Libera toda la memoria y deja el array de incidencias vacío. Reinicia contId a 0.
// t es el tamaño del array.
// 1 pt.
void destruirIncidencias(ListaIncidencias *array, int t);

// Cambiar prioridad a incidencia existente. Se recomienda hacer una función auxiliar que devuelva la prioridad de una incidencia dado su id.
// t es el tamaño del array.
// 1.75 pt.
void cambiarPrioridad(ListaIncidencias *array, int id, int nuevaPrioridad, int t);

// Establecer la evaluación con valor valorEvaluacion a la incidencia con id existente.
// Si el árbol está vacío, se devuelve NULL.
// t es el tamaño del array.
// 0.5 pt.
void evaluarIncidencia(ListaIncidencias *array, int id, int valorEvaluacion, int t);

// Guarda en un fichero de texto los datos de las incidencias almacenadas en la lista de incidencias. El formato del fichero de texto será el siguiente, primero tendrá una cabecera con una descripción de los campos. Tras esta cabecera, una línea por cada incidencia, ordenadas por prioridad primero y luego por antigüedad (las más antiguas primero). En caso de no estar evaluada una incidencia, el campo valor será -1;

// Prioridad;Descripcion;Puntacion;
// 0;Puerta 002 no cierra correctamente;-1;
// 0;Puerta 004 no cierra correctamente;5;
// 9;Puerta 904 no cierra correctamente;100;

// t es el tamaño del array.
// 1.75 pts.
void guardarRegistroIncidencias(char *filename, ListaIncidencias *array, int t);

// Lee de fichero binario los datos de incidencias y los carga para su uso. El array puede no estar vario, recuerda antes borrar todas las incidencias existentes.
// Cada incidencia es almacenada en el fichero con la siguiente estructura:
//- Un entero con la prioridad de la incidencia.
//- Un entero con el tamaño del campo descripción.
//- La cadena de caracteres con la descripción, incluido el carácter terminador '\0'.
//- Un entero con la puntuación.
// Se asume que las incidencias están guardadas por antigüedad, siendo las primeras las más antiguas.
// t es el tamaño del array.
// 2.0 pts.
void cargarRegistroIncidencias(char *filename, ListaIncidencias *array, int t);

#endif /* INCIDENCIAS_H_ */
