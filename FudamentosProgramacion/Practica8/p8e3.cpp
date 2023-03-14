#include <iostream>
#include <string>

using namespace std;

struct Anagrama {
  string palabra;
  int repeticiones = -1;
};

string ordenarPalabra(const string &palabra) {
  string Npalabra = palabra;
  for (int i = 0; i < int(palabra.size()); i++) {
    for (int j = 0; j < int(Npalabra.size()); j++) {
      if (Npalabra[j] > Npalabra[i]) {
        char aux = Npalabra[i];
        Npalabra[i] = Npalabra[j];
        Npalabra[j] = aux;
      }
    }
  }
  return Npalabra;
}

bool esAnagrama(const string &palabraBase, const string &palabra) {
  string NpalabraBase = ordenarPalabra(palabraBase);
  string Npalabra = ordenarPalabra(palabra);
  return NpalabraBase == Npalabra;
}

Anagrama procesarCadena(string &cadena) {
  Anagrama anagrama;
  cout << "Introduzca el texto en minúsculas hasta (fin) con el anagrama a "
          "comprobar al princio."
       << endl;
  cin >> cadena;
  anagrama.palabra = cadena;

  while (cadena != "fin") {
    if (esAnagrama(anagrama.palabra, cadena)) {
      anagrama.repeticiones++;
    }

    cin >> cadena;
  }
  return anagrama;
}

void mostrarAnagramas(Anagrama &anagrama) {
  cout << "En este texto hay " << anagrama.repeticiones << " anagramas como <"
       << anagrama.palabra << ">." << endl;
}

int main() {
  string cadena;
  Anagrama anagrama = procesarCadena(cadena);
  mostrarAnagramas(anagrama);
}
