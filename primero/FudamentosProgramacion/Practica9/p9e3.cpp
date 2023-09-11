#include <array>
#include <iostream>
#include <string>

using namespace std;

const int MAXCOL = 12;
const int MAXFIL = 7;

typedef array<char, MAXCOL> Lista;
typedef array<Lista, MAXFIL> Matriz;

void leerImagen(Matriz &matriz) {
  cout << "Introduce la imagen de 7 x 12 caracteres: " << endl;
  for (int f = 0; f < int(matriz.size()); f++) {
    for (int c = 0; c < int(matriz[f].size()); c++) {
      cin >> matriz[f][c];
    }
  }
}

void getEdges(const Matriz &matriz, int &fline, int &lline) {
  fline = -1;
  lline = -1;

  for (int f = 0; f < int(matriz.size()); f++) {
    for (int c = 0; c < int(matriz[f].size()); c++) {
      if (matriz[f][c] == '*') {
        if (fline < 0) {
          fline = f;
        }
        lline = f;
      }
    }
  }
}

int getDist(const int &fline, const int &lline) { return (lline - fline) + 1; }

int main() {
  Matriz matriz;
  int fline;
  int lline;

  leerImagen(matriz);
  getEdges(matriz, fline, lline);
  int diameter = getDist(fline, lline);
  cout << "Resultado: " << diameter << endl;
}
