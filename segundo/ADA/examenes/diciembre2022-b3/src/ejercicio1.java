import java.util.ArrayList;

public class ejercicio1 {
  private int K; // numero de personas
  private int M; // numero de objetos
  private ArrayList<Integer> unidades;
  private ArrayList<ArrayList<Integer>> preferencias;
  private ArrayList<Integer> resultado;

  public ArrayList<Integer> getResultado(){
    return resultado;
  }

  public ejercicio1(int K, int M, ArrayList<Integer> unidades,
                    ArrayList<ArrayList<Integer>> preferencias) {
    this.K = K;
    this.M = M;
    this.unidades = unidades;
    this.preferencias = preferencias;
    this.resultado = new ArrayList<>();
  }

  private ArrayList<Integer> encontrarCandidatos(ArrayList<Integer> unidades,
                                                 ArrayList<ArrayList<Integer>> preferencias, int persona){
    ArrayList<Integer> candidatos = new ArrayList<>();
    for(Integer v : preferencias.get(persona)){
      if(unidades.get(v-1) > 0){
        candidatos.add(v);
      }
    }
    return candidatos;
  }

  private boolean esCompleta(ArrayList<Integer> solucion){
    return solucion.size() == K;
  }

  public boolean resolverVA(ArrayList<Integer> solucion){
    if(esCompleta(solucion)){
      return true;
    }else{
      boolean haySol = false;
      ArrayList<Integer> candidatos = encontrarCandidatos(unidades,
          preferencias,solucion.size());

      int idxCandidato = 0;
      while (!haySol && idxCandidato <candidatos.size()){
        solucion.add(candidatos.get(idxCandidato));
        unidades.set(candidatos.get(idxCandidato)-1,
            unidades.get(candidatos.get(idxCandidato)-1)-1);
        haySol = resolverVA(solucion);

        if(!haySol){
          unidades.set(solucion.get(solucion.size()-1)-1,
              unidades.get(solucion.get(solucion.size()-1)-1)+1);
          solucion.remove(solucion.size()-1);
        }
        idxCandidato++;
      }
      return haySol;
    }
  }
}
