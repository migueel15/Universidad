#include <array>
#include <iomanip>
#include <iostream>
#include <string>
using namespace std;

const int MAXCOL = 7;
const int MAXFILA = 5;

typedef array<char, MAXCOL> Fila;
typedef array<Fila, MAXFILA> SalaCine;

void inicializar(SalaCine &sc) {
  for (int f = 0; f < int(sc.size()); f++) {
    for (int c = 0; c < int(sc[f].size()); c++) {
      sc[f][c] = 'o';
    }
  }
}

void leerArray(SalaCine &sc) {
  for (int f = 0; f < int(sc.size()); f++) {
    for (int c = 0; c < int(sc[f].size()); c++) {
      cin >> sc[f][c];
    }
  }
}

void mostrarGuiones() {
  cout << setw(16) << right << "0 1 2 3 4 5 6" << endl;
  cout << setw(16) << "----------------" << endl;
}
void mostrar(const SalaCine &sc) {
  mostrarGuiones();

  for (int f = 0; f < int(sc.size()); f++) {
    cout << f << ": ";
    for (int c = 0; c < int(sc[f].size()); c++) {
      cout << sc[f][c] << " ";
    }
    cout << endl;
  }
}

void comprar_ticket_consecutivo(SalaCine &sc, int fila_1, int fila_2, int n,
                                bool &ok, int &fil_sel, int &col_sel) {
  ok = false;
  for (int f = fila_1; f <= fila_2 && !ok; f++) {
    for (int c = 0; c < int(sc[f].size()) && !ok; c++) {
      if (sc[f][c] == 'o') {
        bool hayAsientos = true;
        for (int num = 1; num < n && hayAsientos; num++) {
          int proxAiento = c + num;

          if (proxAiento >= int(sc[f].size()) || sc[f][proxAiento] != 'o') {
            hayAsientos = false;
          }
        }
        ok = hayAsientos;
        if (ok) {
          fil_sel = f;
          col_sel = c;
        }
      }
    }
  }
  if (ok) {
    for (int i = 0; i < n; i++) {
      sc[fil_sel][col_sel + i] = 'x';
    }
  }
}

void cancelar_ticket(SalaCine &sc, int fila, int columna, bool &ok) {
  ok = false;
  if (sc[fila][columna] == 'x') {
    sc[fila][columna] = 'o';
    ok = true;
  }
}

int main() {
  SalaCine salaCine;
  bool ok;
  int fil_sel = -1;
  int col_sel = -1;
  inicializar(salaCine);
  leerArray(salaCine);
  mostrar(salaCine);
  comprar_ticket_consecutivo(salaCine, 2, 4, 2, ok, fil_sel, col_sel);
  mostrar(salaCine);
  cancelar_ticket(salaCine, 2, 3, ok);
  mostrar(salaCine);
}

/*
x x o o x o o
o o o o o o o
o x o o o x o
x x x x o o o
x o o o o o x
*/
