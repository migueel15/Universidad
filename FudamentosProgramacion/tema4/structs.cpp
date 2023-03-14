#include <iostream>
using namespace std;

struct Tiempo {
  int hor;
  int min;
  int sec;
};

void leer_tiempo(Tiempo &tiempo) {
  cout << "Introduce tiempo: ";
  cin >> tiempo.hor >> tiempo.min >> tiempo.sec;
}

int calcular_seg(Tiempo &tiempo) {
  int segundos = tiempo.hor * 3600 + (tiempo.min * 60) + tiempo.sec;
  return segundos;
}

int calcular_resta(int t1, int t2) {
  int res;
  if (t1 > t2) {
    res = t1 - t2;
  } else {
    res = t2 - t1;
  }
  return res;
}

void formatear(Tiempo &tiempo, int segundos) {
  int horas = segundos / 3600;
  int segRes = segundos % 3600;
  int minutos = segRes / 60;
  int seg = segRes % 60;
  tiempo.hor = horas;
  tiempo.min = minutos;
  tiempo.sec = seg;
}

void mostrar_tiempo(const Tiempo &respuestaFormat) {
  cout << respuestaFormat.hor << " horas, " << respuestaFormat.min << " minutos y "
       << respuestaFormat.sec << " segundos" << endl;
}

int main() {
  Tiempo t1;
  Tiempo t2;
  Tiempo respuestaFormat;
  leer_tiempo(t1);
  leer_tiempo(t2);
  int sec1 = calcular_seg(t1);
  int sec2 = calcular_seg(t2);
  int resSec = calcular_resta(sec1, sec2);
  formatear(respuestaFormat, resSec);
  mostrar_tiempo(respuestaFormat);
}