public class PruebaLaberintoSumergido {
  public static void main(String [] args) {
    int[][]maze =
        {{ 0, -1,  0,  0, -1, -1},
            { 1,  1,  0, -1,  1, -1},
            {-1,  1, -1,  1,  2, 0},
            { 0,  0,  7,  5, -1, 0},
            {-1,  0, -1,  6, -1, 0},
            { 0,  1,  1 , 1,  0, 1}};

    LaberintoSumergido p = new LaberintoSumergido(maze,new Posicion(0,0),new Posicion(2,3),11);
    System.out.println("MejorVA->" + p.encontrarCaminoOptimo());

    LaberintoSumergido pNo = new LaberintoSumergido(maze,new Posicion(0,0),new Posicion(2,3),10);
    System.out.println("MejorVA->" + pNo.encontrarCaminoOptimo());

  }

}