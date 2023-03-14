#include <array>
#include <iostream>
#include <string>
using namespace std;

const int MAXLETRAS = 5;
const int MAXPALABRAS = 10;

typedef array<string, MAXPALABRAS> ArrayPalabras;

struct s_Letra {
  char letra;
  ArrayPalabras arrayPalabras = {};
  int maxPalabras = 0;
};

typedef array<s_Letra, MAXLETRAS> ArrayLetras;

struct s_ArrayLetras {
  ArrayLetras arrayLetras;
  int maxLetras = 0;
};

int existe(s_ArrayLetras &arrayLetras, char letra) {
  bool existe = false;
  int index = -1;
  for (int i = 0; i < arrayLetras.maxLetras && !existe; i++) {
    if (letra == arrayLetras.arrayLetras[i].letra) {
      existe = true;
      index = i;
    }
  }
  return index;
}

void leerLetras(s_ArrayLetras &arrayLetras) {
  string palabra;
  cout << "Introduzca un patron (longitud maxima = 5): ";
  cin >> palabra;

  int pos = 0;
  for (int l = 0; l < int(palabra.size()); l++) {
    if (existe(arrayLetras, palabra[l]) < 0) {
      arrayLetras.arrayLetras[pos].letra = palabra[l];
      arrayLetras.maxLetras++;
      pos++;
    }
  }
}

bool existeLetra(char &letra, string &palabra) {
  bool existe = false;
  for (int i = 0; i < int(palabra.size()) && !existe; i++) {
    if (letra == palabra[i]) {
      existe = true;
    }
  }
  return existe;
}

void procesarPalabra(string &palabra, s_ArrayLetras &arrayLetras) {
  for (int i = 0; i < int(arrayLetras.arrayLetras.size()); i++) {
    if (existeLetra(arrayLetras.arrayLetras[i].letra, palabra)) {
      arrayLetras.arrayLetras[i]
          .arrayPalabras[arrayLetras.arrayLetras[i].maxPalabras] = palabra;
      arrayLetras.arrayLetras[i].maxPalabras++;
    }
  }
}

void leerFrase(s_ArrayLetras &arrayLetras) {
  string palabra;
  cin >> palabra;
  while (palabra != "FIN") {
    procesarPalabra(palabra, arrayLetras);
    cin >> palabra;
  }
}

void mostrarArrayLetras(s_ArrayLetras &arrayLetras) {
  for (int i = 0; i < int(arrayLetras.maxLetras); i++) {
      cout << arrayLetras.arrayLetras[i].letra << ": ";
    for (int j = 0; j < int(arrayLetras.arrayLetras.size()); j++) {
      cout << arrayLetras.arrayLetras[i].arrayPalabras[j] << " ";
    }

    cout << endl;
  }
}

int main() {
  s_ArrayLetras arrayLetras;
  leerLetras(arrayLetras);
  leerFrase(arrayLetras);
  mostrarArrayLetras(arrayLetras);
}
