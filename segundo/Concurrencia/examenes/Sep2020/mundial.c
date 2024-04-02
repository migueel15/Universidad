/*
 ============================================================================
 Nombre y Apellidos: Miguel Angel Dorado Maldonado
 DNI: xxxx
 Titulación y Grupo: Software A
 Ordenador:
 ============================================================================
 */

#include "stdio.h"
#include "stdlib.h"
#include "string.h"

#include "mundial.h"

/**
 * Crea una lista vacia.
 */
Lista Lista_Crear() { return NULL; }

/**
 * Imprime por consola el contenido de cada uno de los nodos de la lista.
 */
void Lista_Imprimir(Lista lista) {
  Lista ptr = NULL;
  ptr = lista;
  printf("***************************\n");
  printf("*ESTADO ACTUAL DE LA LISTA*\n");
  printf("***************************\n");
  if (ptr == NULL) {
    printf("Lista vacia......\n");
  } else {
    while (ptr != NULL) {
      printf("===============================================\n");
      printf("Equipo: %s\n", ptr->nombre);
      printf("Victorias: %d\n", ptr->victorias);
      printf("Goles: %d\n", ptr->goles);
      ptr = ptr->sig;
    }
  }
}

/**
 * Comprueba si en la lista hay un nodo cuyo nombre coincida con
 * el que se pasa como parametro.
 * Devuelve 1 si lo encuentra, 0 en otro caso.
 */
int Esta(Lista lista, char *elem) {
  while (lista != NULL) {
    if (strcmp(lista->nombre, elem) == 0) {
      return 1;
    }
    lista = lista->sig;
  }
  return 0;
}

/**
 * Recibe la información para insertar un nuevo equipo.
 * Si el equipo no está en la lista, lo inserta al PRINCIPIO
 * de la lista y se devuelve un 1.
 * En otro caso, no se inserta y se devuelve un 0.
 */
int Lista_Agregar_Al_Principio(Lista *lista, int vict, int goles, char *nom) {
  int esta = Esta((*lista), nom);
  if (esta != 1) {
    Lista new = malloc(sizeof(struct Nodo));
    (*new).victorias = vict;
    (*new).goles = goles;
    strcpy(new->nombre, nom);
    (*new).sig = *lista;
    (*lista) = new;
    return 1;
  }
  return 0;
}

/**
 * Elimina de la lista el equipo cuyo nombre coincida con el que
 * se pasa como parametro y se devuelve un 1.
 * Si no se encuentra el equipo, no elimina nada y devuelve un 0.
 */
int Eliminar_Equipo(Lista *lista, char *nombre) {
  int r = 0;
  Lista ant = NULL;
  Lista act = *lista;

  // si está al final
  while (act != NULL && !r) {
    if (strcmp(act->nombre, nombre) == 0) {
      r = 1;
    } else {
      ant = act;
      act = act->sig;
    }
  }

  if (r != 0) {
    if (ant == NULL) {
      *lista = act->sig;
      free(act);
    } else {
      ant->sig = act->sig;
      free(act);
    }
  }

  return r;
}

/**
 * Elimina todos los equipos de la lista, liberando toda la memoria.
 */
void Lista_Destruir(Lista *lista) {
  while (*lista != NULL) {
    Eliminar_Equipo(lista, (*lista)->nombre);
    *lista = (*lista)->sig;
  }
}

/*
 * HASTA AQUI PARA APROBAR
 */

/**
 * Carga desde el fichero resultadosCuartos.txt
 * en modo texto el contenido de la lista de equipos
 * con los resultados de esa fase.
 */
void Lista_Cargar(Lista *lista) {
  FILE *fd = fopen("resultadosOctavos.txt", "r");
  if (fd == NULL) {
    perror("Error al leer de archivo.");
  }
  char *nombre;
  int victorias;
  int goles;

  while (fscanf(fd, "%3s%d%d", nombre, &victorias, &goles) == 3) {
    Lista_Agregar_Al_Principio(lista, victorias, goles, nombre);
  }
  fclose(fd);
}

/**
 * Traslada de la lista que recibe como primer parametro
 * a la lista que recibe como segundo parametro, los
 * nodos cuyo valor de victorias sea cero.
 * Además, los elimina de la primera lista.
 */
void Trasladar_Descalificados(Lista *lista, Lista *listaDescalificados) {
  while (*lista != NULL) {
    if ((*lista)->victorias == 0) {
      Lista_Agregar_Al_Principio(listaDescalificados, (*lista)->victorias,
                                 (*lista)->goles, (*lista)->nombre);
      Eliminar_Equipo(lista, (*lista)->nombre);
    }
    *lista = (*lista)->sig;
  }
}

/*
 * HASTA AQUI PARA NOTABLE
 */

/**
 * Calcula y devuelve el recuento de goles acumulados
 * por el equipo mas anotador de la fase de OCTAVOS.
 */
int Calcular_Maximos_Goles(Lista lista) {
  int max = -1;
  while (lista != NULL) {
    if (lista->goles >= max) {
      max = lista->goles;
    }
  }
  return max;
}

/**
 * Genera una nueva lista enlazada de equipos ordenada en base
 * al número de goles anotados, de MENOR a MAYOR.
 * Es decir, el primer nodo será el equipo menos goleador y el
 * último, el equipo más goleador.
 * Si hay equipos con el mismo número de goles anotados,
 * aparecerán consecutivos pero no importa el orden entre ellos.
 * Devuelve la lista generada.
 */
Lista Crear_Lista_Maximos_Goleadores(Lista lista) {
  Lista nuevaLista = NULL;
  while (lista != NULL) {
    Lista buscador = lista;
    int goles = Calcular_Maximos_Goles(lista);
    while (buscador->goles != goles) {
      buscador = buscador->sig;
    }
    Lista_Agregar_Al_Principio(&nuevaLista, buscador->victorias,
                               buscador->goles, buscador->nombre);
    Eliminar_Equipo(&lista, buscador->nombre);
  }

  return nuevaLista;
}

// Fin fichero
// ===========
