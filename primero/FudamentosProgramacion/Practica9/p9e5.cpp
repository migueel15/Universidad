#include <array>
#include <iostream>
#include <string>
using namespace std;
const int MAX_COL = 6;
const int MAX_FIL = 6;
typedef array<char, MAX_COL> M_Cols;
typedef array<M_Cols, MAX_FIL> Matriz;

const Matriz CLAVE = {{
    {{'p', 'k', 'a', 'f', '5', 'v'}},
    {{'e', '9', 'o', 't', 'y', '0'}},
    {{'s', '3', 'z', '7', 'd', 'j'}},
    {{'r', 'n', 'b', 'u', 'm', '1'}},
    {{'2', 'w', '4', 'h', '8', 'g'}},
    {{'c', 'x', '6', 'q', 'i', 'l'}},
}};

bool checkImpar(string &palabra) { return int(palabra.size()) % 2 != 0; }

string buscarPalabras(string &palabra, int size, Matriz clave) {
  string palabraCifrada;
  int index = 0;
  for (int i = 0; i < size / 2; i++) {
    int i_1f;
    int i_1c;
    int i_2f;
    int i_2c;

    for (int f = 0; f < int(clave.size()); f++) {
      for (int c = 0; c < int(clave[f].size()); c++) {
        if (palabra[index] == clave[f][c]) {
          i_1f = f;
          i_1c = c;
        } else if (palabra[index + 1] == clave[f][c]) {
          i_2f = f;
          i_2c = c;
        }
      }
    }

    palabraCifrada += clave[i_1f][i_2f];
    palabraCifrada += clave[i_1c][i_2c];
    index += 2;
  }

  return palabraCifrada;
}

string cifrar(string &palabra) {
  int size = 0;
  bool esImpar = checkImpar(palabra);
  if (esImpar) {
    size = int(palabra.size()) - 1;
  } else {
    size = int(palabra.size());
  }

  return buscarPalabras(palabra, size, CLAVE);
}

int main() {
  string palabra;
  cin >> palabra;
  string pCifrada = cifrar(palabra);
  cout << "Normal: " << palabra << " Cifrado: " << pCifrada << endl;
}
