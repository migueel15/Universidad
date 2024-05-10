#include "Procesos.h"
#include <stdio.h>
#include <stdlib.h>

/**
 * (0.25 puntos)
 * @brief Crea una lista de procesos vacia
 * @param lp lista de procesos que se creara
 */
void crear(LProcesos *lp) { *lp = NULL; }

/**
 * (2 puntos)
 * @brief Inserta un proceso en la lista de procesos. La lista esta ordenada por
 * la prioridad de los procesos, de mayor a menor prioridad. La prioridad será
 * un valor entre 1 y 5, indicando el valor 1 la prioridad mínima y 5 la máxima.
 * Todos los procesos con la misma prioridad estarán consecutivos en la lista,
 * pero sin un orden establecido entre ellos.
 *
 * - Si el tiempo es 0, la prioridad no es valida, o el tiempo no es multiplo de
 * INTERVALO, el proceso no se inserta
 * - Si ya existe un proceso con el mismo id en la lista, no se creará un nuevo
 * nodo sino que los tiempos de ejecucion se sumarán (su puede asumir que si ya
 * existe un proceso con el mismo id, la prioridad también será la misma)
 *
 * @param lp lista de procesos donde insertar
 * @param prioridad prioridad del proceso a insertar
 * @param id identificador del proceso a insertar
 * @param tiempo tiempo de ejecucion del proceso a insertar
 */
LProcesos crearNodo(unsigned prioridad, unsigned id, unsigned tiempo) {
  LProcesos nodo = malloc(sizeof(struct Proceso));
  if (nodo == NULL) {
    perror("No se ha podido crear el nodo");
    exit(1);
  }
  nodo->prioridad = prioridad;
  nodo->id = id;
  nodo->tiempo = tiempo;
  nodo->sig = NULL;
  return nodo;
}

void insertar(LProcesos *lp, unsigned prioridad, unsigned id, unsigned tiempo) {
  if (tiempo == 0 || prioridad < MIN_PRIORIDAD || prioridad > MAX_PRIORIDAD ||
      (tiempo % INTERVALO != 0)) {
    // no se inserta
  } else {
    if (*lp == NULL) {
      LProcesos nodo = crearNodo(prioridad, id, tiempo);
      (*lp) = nodo;
    } else {
      int fin = 0;
      LProcesos ant = NULL;
      LProcesos act = *lp;
      while (act != NULL && !fin) {
        if (act->id == id) {
          fin = 1;
        } else if (prioridad > act->prioridad) {
          fin = 1;
        } else {
          ant = act;
          act = act->sig;
        }
      }

      if (ant == NULL) {
        // insertamos al principio
        if (act->id == id) {
          act->tiempo += tiempo;
        } else {
          LProcesos nodo = crearNodo(prioridad, id, tiempo);
          nodo->sig = (*lp);
          (*lp) = nodo;
        }
      } else if (act == NULL) {
        // insertamos al final
        if (ant->id == id) {
          ant->tiempo += tiempo;
        } else {
          LProcesos nodo = crearNodo(prioridad, id, tiempo);
          ant->sig = nodo;
        }
      } else {
        if (act->id == id) {
          // añadir valores al nodo que ya existe
          act->tiempo += tiempo;
        } else {
          // crear nuevo nodo
          LProcesos nodo = crearNodo(prioridad, id, tiempo);
          nodo->sig = act;
          ant->sig = nodo;
        }
      }
    }
  }
}

/**
 * (0.5 puntos)
 * @brief Muestra el contenido de la lista de procesos
 * Si la lista esta vacia no se muestra nada
 * Si la lista contiene elementos, el formato sera el siguiente:
 * [<prioridad>, <id>, <tiempo>] [<prioridad>, <id>, <tiempo>] ...
 * En el enunciado se muestra la salida al ejecutar el fichero driver.c
 * y ahi podeis ver un ejemplo de como se muestra la lista de procesos
 * @param lp lista de procesos a mostrar
 */
void mostrar(LProcesos lp) {
  while (lp != NULL) {
    printf("[%d, %d, %d] ", lp->prioridad, lp->id, lp->tiempo);
    lp = lp->sig;
  }
  printf("\n");
}

/**
 * (0.5 puntos)
 * @brief Guarda en un fichero los procesos de la lista de procesos que tienen
 *una determinada prioridad. Si la prioridad existe en la lista, el formato del
 *fichero será el siguiente: Prioridad <prioridad> id:<id> tiempo:<tiempo>
 * 		id:<id> tiempo:<tiempo>
 *		...
 * Si la prioridad no existe en la lista, el formato del fichero será el
 *siguiente: Prioridad <prioridad> Sin procesos
 * @param lp lista de procesos a mostrar
 * @param prioridad prioridad de los procesos a guardar en el fichero
 * @param fd descriptor de un fichero de texto
 */
void guardarFicheroPrioridad(LProcesos lp, unsigned prioridad, FILE *fd) {
  int contador = 0;
  fprintf(fd, "Prioridad %d\n", prioridad);
  while (lp != NULL) {
    if (lp->prioridad == prioridad) {
      // añadimos el proceso
      fprintf(fd, "\tid:%d tiempo:%d\n", lp->id, lp->tiempo);
      contador++;
    }
    lp = lp->sig;
  }
  if (contador == 0) {
    fprintf(fd, "\tSin procesos\n");
  }
}

/**
 * (1.5 puntos)
 * @brief Ejecuta tantos procesos de la lista como numero de procesadores se
 * indiquen en el tercer parametro, de mayor a menor prioridad. Cada proceso se
 * ejecuta una sola vez, durante el tiempo indicado por el segundo parametro.
 *
 * Por ejemplo, si el numero de procesadores en el tercer parametro es 4 y el
 * tiempo de ejecucion en el segundo parametro es 10, se ejecutaran los primeros
 * 4 procesos (o los que haya para ejecutar si son menos de 4 en la lista)
 * durante un tiempo de ejecucion 10.
 *
 *  - Si la lista esta vacia no se hace nada
 *  - Si el tiempo es 0 o no es múltiplo de INTERVALO no se hara nada
 *  - Ejecutar un proceso significa reducir su tiempo en el tiempo de ejecucion
 * indicado como segundo parametro. Si el tiempo despues de ejecutar ese proceso
 * es 0, el proceso es eliminado de la lista.
 *  - Si el valor del parametro 'tiempo' es mayor que el tiempo de ejecucion del
 * proceso entonces el proceso se ejecutara de forma completa, y habra que
 * eliminarlo de la lista al ser su tiempo restante de ejecucion 0.
 *  - Al terminar la ejecucion se devuelve el numero de procesadores no
 * utilizados o 0 si se han utilizado todos
 * @param lp lista de procesos
 * @param tiempo tiempo durante el cual se ejecutara cada proceso
 * @param num_proc numero de procesadores disponibles
 * @return numero de procesadores no utilizados en la ejecucion
 */
unsigned ejecutarMultiplesProcesadores(LProcesos *lp, unsigned tiempo,
                                       unsigned num_proc) {
  if (*lp != NULL && tiempo != 0 && tiempo % INTERVALO == 0) {
    LProcesos ant = NULL;
    LProcesos act = *lp;

    while (num_proc != 0 && act != NULL) {
      LProcesos borrar = act;
      // reducimos el tiempo
      if (act->tiempo > tiempo) {
        act->tiempo -= tiempo;
      } else {
        act->tiempo = 0;
      }

      // si el tiempo es menor o igual a 0 lo borramos
      if (act->tiempo == 0) {
        if (ant == NULL) {
          // borrar al principio
          (*lp) = act->sig;
          free(borrar);
        } else {
          // borrar mitad/final
          ant->sig = act->sig;
          free(borrar);
        }
      }

      ant = act;
      act = act->sig;
      num_proc--;
    }
  }
  return num_proc;
}

/**
 * (1 punto)
 * @brief Ejecuta un proceso de la lista (el que tenga mayor prioridad) tantas
 * veces como se indique y durante un determinado periodo de tiempo cada vez. Se
 * asume que se tiene un solo procesador
 *  - Si la lista esta vacia no se hace nada
 *  - Si el tiempo es 0 o no es múltiplo de INTERVALO no se hará nada
 *  - Ejecutar el proceso significa reducir su tiempo en el tiempo de ejecución
 * indicado como segundo parametro. Si el tiempo despues de ejecutar un proceso
 * es 0, el proceso es eliminado de la lista y si la ejecucion no ha terminado
 * se seguira con el siguiente proceso en la lista
 *  - Si el valor del parametro 'tiempo' es mayor que el tiempo de ejecucion del
 * proceso entonces el proceso se ejecutara de forma completa, y habra que
 * eliminarlo de la lista al ser su tiempo restante de ejecucion 0.
 *
 * @param lp lista de procesos
 * @param veces numero de veces que se ejecutará
 * @param tiempo tiempo durante el cual se ejecutara el proceso
 */
void ejecutarMultiplesVeces(LProcesos *lp, unsigned tiempo, unsigned veces) {
  if (*lp != NULL && tiempo != 0 && tiempo % INTERVALO == 0) {

    LProcesos act = *lp;
    while (veces != 0 && act != NULL) {
      LProcesos borrar = act;
      if (act->tiempo > tiempo) {
        // no se avanza y se actualiza
        act->tiempo -= tiempo;
      } else if (act->tiempo <= tiempo) {
        // borrar y pasar al siguiente
        act = act->sig;
        free(borrar);
      }

      veces--;
    }
    *lp = act;
  }
}

/**
 * (1.5 puntos)
 * Devuelve una lista nueva con todos los procesos de la lista de procesos que
 * tienen una determinada prioridad. Los procesos se eliminarán de la lista
 * original.
 *
 * - Si no hay ningun proceso con esa prioridad se devolvera una lista vacia
 *
 * @param lp lista de procesos
 * @param prioridad prioridad de los procesos que se quieren devolver en la
 * nueva lista
 * @return lista de procesos en @param lp con la prioridad indicada en @param
 * prioridad
 */
LProcesos extraerProcesos(LProcesos *lp, unsigned prioridad) {
  LProcesos nuevaLista = NULL;
  LProcesos ant = NULL;
  LProcesos act = (*lp);
  while (act != NULL) {
    if (act->prioridad == prioridad) {
      // añadir
      insertar(&nuevaLista, act->prioridad, act->id, act->tiempo);
      // borrar de la anterior
      if (ant == NULL) {
        // borrar del inicio
        LProcesos borrar = *lp;
        *lp = (*lp)->sig;
        free(borrar);
      } else {
        // borrar del medio
        LProcesos borrar = act;
        ant->sig = act->sig;
        free(borrar);
      }
    }
    ant = act;
    act = act->sig;
  }
  (*lp) = (*lp)->sig;
  return nuevaLista;
}

/**
 * (0.5 puntos)
 * @brief Elimina todos los nodos y deja la lista vacía
 * @param lp lista de procesos a borrar
 */
void borrar(LProcesos *lp) {
  while (*lp != NULL) {
    LProcesos aux = *lp;
    *lp = (*lp)->sig;
    free(aux);
  }
}
