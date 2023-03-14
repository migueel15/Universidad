#include <array>
#include <iostream>

using namespace std;

const int MAXCARGOS = 15;
const int MAXPARTIDOS = 10;

struct Partido {
  char nombre = ' ';
  int votos = 0;
  int cargosElectos = 0;
};

struct ConfArrays {
  int cargosMax;
  int partidosMax;
};

typedef array<Partido, MAXPARTIDOS> ListaPartidos;
typedef array<int, MAXCARGOS * MAXPARTIDOS> ArrayHondt;

void leerDatos(ListaPartidos &datosPartidos, ConfArrays &config) {

  cout << "Introduzca el Numero de Cargos (>= 1 y <= 15): ";
  cin >> config.cargosMax;
  cout << "Introduzca el Numero de Partidos (>= 1 y <= 10): ";
  cin >> config.partidosMax;
  cout << "Introduzca el Nombre y Numero de Votos por Partido: " << endl;
  for (int i = 0; i < config.partidosMax; i++) {
    cout << "Partido " << i + 1 << ": ";
    cin >> datosPartidos[i].nombre;
    cin >> datosPartidos[i].votos;
  }
}

void crearHondt(ListaPartidos &datosPartidos, ConfArrays &config,
                ArrayHondt &Hondt) {
  Hondt = {};
  int i = 0;
  while (i < config.cargosMax * config.partidosMax) {
    for (int ipartido = 0; ipartido < config.partidosMax; ipartido++) {
      for (int icargo = 1; icargo <= config.cargosMax; icargo++) {
        Hondt[i] = datosPartidos[ipartido].votos / icargo;
        i++;
      }
    }
  }
}

void ordenarHondt(ArrayHondt &Hondt) {
  for (int f = 0; f < int(Hondt.size()) - 1; f++) {
    for (int i = 0; i < int(Hondt.size()) - 1; i++) {
      if (Hondt[i] < Hondt[i + 1]) {
        int aux = Hondt[i];
        Hondt[i] = Hondt[i + 1];
        Hondt[i + 1] = aux;
      }
    }
  }
}

void calcularCargos(ListaPartidos &datosPartidos, ConfArrays &config) {
  ArrayHondt aHondt = {};
  crearHondt(datosPartidos, config, aHondt);
  ordenarHondt(aHondt);

  for (int partido = 0; partido < config.partidosMax; partido++) {
    for (int division = 1; division < config.cargosMax; division++) {
      for (int iHondt = 0; iHondt < config.cargosMax; iHondt++) {
        if (datosPartidos[partido].votos / division == aHondt[iHondt]) {
          datosPartidos[partido].cargosElectos++;
        }
      }
    }
  }
}

void mostrar(ListaPartidos &datosPartidos, ConfArrays &config) {
  cout << "Los Cargos Electos son: " << endl;
  for (int i = 0; i < config.partidosMax; i++) {
    if (datosPartidos[i].cargosElectos != 0) {
      cout << datosPartidos[i].nombre << " " << datosPartidos[i].cargosElectos
           << endl;
    }
  }
}

int main() {
  ListaPartidos listaPartidos;
  ConfArrays configuracion;
  leerDatos(listaPartidos, configuracion);
  calcularCargos(listaPartidos, configuracion);
  mostrar(listaPartidos, configuracion);
}