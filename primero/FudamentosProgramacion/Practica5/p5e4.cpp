#include <iostream>
using namespace std;

void leerSecuencia(int &secuencia) {
  cout << "Introduzca una secuencia de numeros terminada en cero: ";
  cin >> secuencia;
}

int sigCifra(int &secuencia) {
  cin >> secuencia;
  return secuencia;
}

bool esZigZag(int secuencia) {

  bool esCorrecto = true, sigMayor = false, primeraVez = true;

  while (secuencia != 0) {
    int a = secuencia;
    int b = sigCifra(secuencia);

    if (b > a && primeraVez) {
      sigMayor = true;
    }
    primeraVez = false;

    if (b > a && sigMayor) {
      sigMayor = false;
    } else if (b < a && !sigMayor) {
      sigMayor = true;
    } else if (b != 0) {
      esCorrecto = false;
    }
  }

  return esCorrecto;
}

void mostrar(bool esCorrecto, int secuencia) {
  if (esCorrecto && secuencia != 0) {
    cout << "La secuencia introducida SI es un zigzag" << endl;
  } else {
    cout << "La secuencia introducida NO es un zigzag" << endl;
  }
}

int main() {
  int secuencia;
  leerSecuencia(secuencia);
  bool resultado = esZigZag(secuencia);
  mostrar(resultado, secuencia);
}