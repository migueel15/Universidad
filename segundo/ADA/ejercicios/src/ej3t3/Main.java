package ej3t3;

public class Main {

  // {v.length > 0}
  //public int primero(int[]v){} // P
  // P = {N(i=0; i<v.length) v[0] == v[i]}

  // {v.length > 0; existe x / v[i] == x siendo i 0..v.length}
  //public int primerIndice(int[] v, int x){} // Q
  // Q = { siendo int entre 0 y v.length; v[Q] == x; para cualquier j entre 0 y Q-1 v[j] != x}

  //caso en q pueda no exister
  // {True}
  // P = {siendo int entre 0 y v.length; v[Q] == x; para cualquier j entre 0 y Q-1 v[j] != x ;return x
  // o si para caulaquier i entre 0 y v.length v[i] != x ;return -1}
}