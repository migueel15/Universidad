#include <array>
#include <iomanip>
#include <iostream>
#include <string>

using namespace std;

const int MAX_ALUMNOS = 20;
const int N_EVALUACIONES = 3;

typedef array<double, N_EVALUACIONES> DatosAlumno;

struct Alumno {
  string nombre;
  DatosAlumno notas;
};

typedef array<Alumno, MAX_ALUMNOS> ListaAl;

struct ListaAlumnos {
  int maxNumer = 0;
  ListaAl array;
};

void leerDatos(ListaAlumnos &listaAlumnos) {
  cout << "Introduce el numero de alumnos de la clase (maximo 20): ";
  cin >> listaAlumnos.maxNumer;
  for (int i = 0; i < int(listaAlumnos.maxNumer); i++) {
    cout << "Introduce el nombre del alumno y sus "
         << int(listaAlumnos.array[i].notas.size()) << " notas: ";
    cin >> listaAlumnos.array[i].nombre;
    for (int f = 0; f < int(listaAlumnos.array[i].notas.size()); f++) {
      cin >> listaAlumnos.array[i].notas[f];
    }
  }
}

double calcMedia(ListaAlumnos &listaAlumnos, int evaluacion) {
  double media = 0;
  for (int i = 0; i < int(listaAlumnos.maxNumer); i++) {
    media += listaAlumnos.array[i].notas[evaluacion];
  }
  media /= int(listaAlumnos.maxNumer);

  return media;
}

void mostrarCabecera() {
  cout << left << setw(10) << "Alumno";
  cout << left << setw(10) << "Nota-1";
  cout << left << setw(10) << "Nota-2";
  cout << left << setw(10) << "Nota-3";
  cout << endl;
  for (int i = (N_EVALUACIONES + 1) * 10; i > 0; i--) {
    cout << "-";
  }
  cout << endl;
}

void mostrarResultados(ListaAlumnos &listaAlumnos) {
  for (int i = 0; i < int(listaAlumnos.maxNumer); i++) {
    cout << left << setw(10) << listaAlumnos.array[i].nombre;
    for (int f = 0; f < int(listaAlumnos.array[i].notas.size()); f++) {
      double media = calcMedia(listaAlumnos, f);
      if (listaAlumnos.array[i].notas[f] >= media) {
        cout << left << setw(10) << "Aprobado";
      } else {
        cout << left << setw(10) << "Suspenso";
      }
    }
    cout << endl;
  }
}

int main() {
  ListaAlumnos listaAlumnos;
  leerDatos(listaAlumnos);
  mostrarCabecera();
  mostrarResultados(listaAlumnos);
}