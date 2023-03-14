#include <iostream>

using namespace std;

struct Complejo {
  double real;
  double img;
};

void leer(Complejo &c) {
  cout << "Introduzca un número complejo (real, img): ";
  cin >> c.real >> c.img;
}

void escribir(const Complejo &c) {
  cout << "( " << c.real << ", " << c.img << " )";
}

void sumar(Complejo &res, const Complejo &c1, const Complejo &c2) {
  res.real = c1.real + c2.real;
  res.img = c1.img + c2.img;
}

void restar(Complejo &res, const Complejo &c1, const Complejo &c2) {
  res.real = c1.real - c2.real;
  res.img = c1.img - c2.img;
}

void multiplicar(Complejo &res, const Complejo &c1, const Complejo &c2) {
  res.real = (c1.real * c2.real) - (c1.img * c2.img);
  res.img = (c1.img * c2.real) + (c1.real * c2.img);
}

void dividir(Complejo &res, const Complejo &c1, const Complejo &c2) {
  // conjugado //
  res.real = (c1.real * c2.real + c1.img * c2.img) /
             (c2.real * c2.real + c2.img * c2.img);
  res.img = (c1.real * -c2.img + c1.img * c2.real) /
            (c2.real * c2.real + c2.img * c2.img);
}

void mostrarSuma(Complejo &res, const Complejo &c1, const Complejo &c2) {
  sumar(res, c1, c2);
  escribir(c1);
  cout << " + ";
  escribir(c2);
  cout << " = ";
  escribir(res);
  cout << endl;
}

void mostrarResta(Complejo &res, const Complejo &c1, const Complejo &c2) {
  restar(res, c1, c2);
  escribir(c1);
  cout << " - ";
  escribir(c2);
  cout << " = ";
  escribir(res);
  cout << endl;
}

void mostarMulti(Complejo &res, const Complejo &c1, const Complejo &c2) {
  multiplicar(res, c1, c2);
  escribir(c1);
  cout << " * ";
  escribir(c2);
  cout << " = ";
  escribir(res);
  cout << endl;
}

void mostrarDivision(Complejo &res, const Complejo &c1, const Complejo &c2) {
  dividir(res, c1, c2);
  escribir(c1);
  cout << " / ";
  escribir(c2);
  cout << " = ";
  escribir(res);
  cout << endl;
}

int main() {
  Complejo c1;
  Complejo c2;
  Complejo res;
  leer(c1);
  leer(c2);
  mostrarSuma(res, c1, c2);
  mostrarResta(res, c1, c2);
  mostarMulti(res, c1, c2);
  mostrarDivision(res, c1, c2);
}