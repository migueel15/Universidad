#include <array>
#include <iostream>

using namespace std;

const int NALUMNOS = 20;
const double MEDIA = 5;
typedef array<double, NALUMNOS> Notas;

void leerNotas(Notas &notas) {
  for (int i = 0; i < int(notas.size()); i++) {
    cout << "Introduce la nota " << i + 1 << " : ";
    cin >> notas[i];
  }
}

void mostrarNota(Notas &notas, double media) {
  for (int i = 0; i < int(notas.size()); i++) {
    if (notas[i] > media) {
      cout << "Alumno " << i + 1 << " Aprobado." << endl;
    } else {
      cout << "Alumno " << i + 1 << " Suspenso." << endl;
    }
  }
}
int main() {
  Notas notasAlumnos;
  leerNotas(notasAlumnos);
  mostrarNota(notasAlumnos, MEDIA);
}