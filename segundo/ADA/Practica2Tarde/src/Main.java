import net.agsh.towerdefense.*;
import net.agsh.towerdefense.Map;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        // initialize game and map
        Game g = Game.getInstance();
        g.init(0);
        Config config = new Config();

        System.out.print("n\tNoSort\tInsertionSort\tMergeSort\tQuickSort\n");

        for(float scale = 0.5f ; scale < 1.5 ; scale += 0.2f) {
            Map map = new Map(new Point2D(config.get(Config.Parameter.MAP_SIZE_X) * scale,
                    config.get(Config.Parameter.MAP_SIZE_Y) * scale),
                    config.get(Config.Parameter.MAP_GRID_SPACE));
            map.init();

            // assign values to nodes and print map
            boolean printMap = false;
            MapNode center = map.getNodes()[map.getNodes().length / 2][map.getNodes()[0].length / 2];
            for (int i = 0; i < map.getNodes().length; i++) {
                for (int j = 0; j < map.getNodes()[i].length; j++) {
                    if (map.getNodes()[i][j].isWalkable()) {
                        if(printMap) {
                            System.out.print("   ");
                        }
                    } else {
                        float distanceToCenter = center.getPosition().distance(map.getNodes()[i][j].getPosition());
                        map.getNodes()[i][j].setValue(0, distanceToCenter);
                        if(printMap) {
                            System.out.printf("%2.0f ", distanceToCenter / 10f);
                        }
                    }
                }
                if(printMap) {
                    System.out.println();
                }
            }

            // select best nodes for towers
            Point2D size = g.getMap().getSize();
            float separation = map.getSeparation();
            int numberOfTowers = (int) (g.getParam(Config.Parameter.TOWER_DENSITY) * size.x * size.y / (separation * separation));
            ArrayList<MapNode> best = selectBestNodes(map.getNodesList(), numberOfTowers);
        }
    }

    private static ArrayList<MapNode> deepCopy(ArrayList<MapNode> nodes) {
        ArrayList<MapNode> copy = new ArrayList<>();
        for(MapNode node : nodes) {
            copy.add(new MapNode(node.getPosition(), node.isWalkable(), node.getValues()));
        }
        return copy;
    }

    private static boolean NodeListEquals(ArrayList<MapNode> a, ArrayList<MapNode> b, int valueIndex) {
        if(a.size() != b.size()) {
            return false;
        }
        for(int i=0; i<a.size(); i++) {
            if(a.get(i).getValue(valueIndex) != b.get(i).getValue(valueIndex)) {
                return false;
            }
        }
        return true;
    }

    private static ArrayList<MapNode>  selectBestNodes(ArrayList<MapNode> nodesList, int count) {
        int n = nodesList.size();
        long maxTime = 1000;

        System.out.print(n+"\t");

        // ========================== No sort ==========================
        int iterations = 0;
        Chronometer c = new Chronometer();
        ArrayList<MapNode> bestNoSort;
        do {
            c.pause();
            ArrayList<MapNode> nodesListCopy = deepCopy(nodesList);
            c.resume();
            bestNoSort = selectBestNodesNoSort(nodesListCopy, count);
            iterations++;
        } while(c.getElapsedTime() < maxTime);
        float timePerIteration = (float) c.getElapsedTime() / iterations;
        System.out.printf("%.4f\t", timePerIteration);

        // ========================== Insertion sort ==========================
        iterations = 0;
        c = new Chronometer();
        ArrayList<MapNode> bestInsertionSort;
        do {
            c.pause();
            ArrayList<MapNode> nodesListCopy = deepCopy(nodesList);
            c.resume();
            bestInsertionSort = selectBestNodesInsertionSort(nodesListCopy, count);
            iterations++;
        } while(c.getElapsedTime() < maxTime);
        timePerIteration = (float) c.getElapsedTime() / iterations;
        System.out.printf("%.4f\t", timePerIteration);

        if(!NodeListEquals(bestNoSort, bestInsertionSort, 0)) {
            System.out.println("ERROR");
        }

        // ========================== Merge sort ==========================
        iterations = 0;
        c = new Chronometer();
        ArrayList<MapNode> bestMergeSort;
        do {
            c.pause();
            ArrayList<MapNode> nodesListCopy = deepCopy(nodesList);
            c.resume();
            bestMergeSort = selectBestNodesMergeSort(nodesListCopy, count);
            iterations++;
        } while(c.getElapsedTime() < maxTime);
        timePerIteration = (float) c.getElapsedTime() / iterations;
        System.out.printf("%.4f\t", timePerIteration);

        if(!NodeListEquals(bestNoSort, bestMergeSort, 0)) {
            System.out.println("ERROR");
        }

        // ========================== Quick sort ==========================
        iterations = 0;
        c = new Chronometer();
        ArrayList<MapNode> bestQuickSort;
        do {
            c.pause();
            ArrayList<MapNode> nodesListCopy = deepCopy(nodesList);
            c.resume();
            bestQuickSort = selectBestNodesQuickSort(nodesListCopy, count);
            iterations++;
        } while(c.getElapsedTime() < maxTime);
        timePerIteration = (float) c.getElapsedTime() / iterations;
        System.out.printf("%.4f\t", timePerIteration);

        if(!NodeListEquals(bestNoSort, bestQuickSort, 0)) {
            System.out.println("ERROR");
        }

        System.out.println();

        return bestNoSort;
    }

    private static ArrayList<MapNode> selectBestNodesNoSort(ArrayList<MapNode> nodesList, int count) {
        ArrayList<MapNode> aux = new ArrayList<>(nodesList);
        ArrayList<MapNode> minimos = new ArrayList<>();

        for(int c = 0; c < count; c++){
            MapNode minNode = aux.get(0);
            for (int k = 0; k < aux.size(); k++) {
                if(aux.get(k).getValue(0) < minNode.getValue(0)){
                    minNode = aux.get(k);
                }
            }
            minimos.add(minNode);
            aux.remove(minNode);
        }
        return minimos;
    }

    private static ArrayList<MapNode> selectBestNodesInsertionSort(ArrayList<MapNode> nodesList, int count) {
        ArrayList<MapNode> resultado = new ArrayList<>();
        for(int i = 0; i < nodesList.size(); i++){
            MapNode nodoActual = nodesList.get(i);
            int pos = -1;
            for(int j = i; j>=0; j--){
                if(nodoActual.getValue(0) < nodesList.get(j).getValue(0)){
                    pos = j;
                }
            }
            if(pos != -1){
                nodesList.remove(nodoActual);
                nodesList.add(pos,nodoActual);
            }
        }
        for(int i = 0; i < count; i++){
            resultado.add(nodesList.get(i));
        }
        return resultado;
    }
    private static float getValue(ArrayList<MapNode> list, int index){
        return list.get(index).getValue(0);
    }
    private static void mezclar(ArrayList<MapNode> a, int inf, int medio, int sup){

        int i = inf;
        int j = medio+1;
        //int[] b = new int[][sup-inf+1];
        ArrayList<MapNode> b = new ArrayList<>();
        int k = 0;

        while(i <= medio && j <= sup) {
            if (getValue(a,i) <= getValue(a,j)) {
                b.add(k, a.get(i));
                i++;
            } else {
                b.add(k, a.get(j));
                j++;
            }
            k++;
        }
        while(i <= medio){
            b.add(k, a.get(i));
            i++;
            k++;
        }
        while(j <= sup){
            b.add(k, a.get(j));
            j++;
            k++;
        }
        k = 0;
        for(int f = inf; f <= sup; f++){
            a.set(f, b.get(k));
            k++;
        }
    }

    private static void ordenar(ArrayList<MapNode> a, int inf, int sup){
        if(inf < sup){
            ordenar(a,inf,(inf+sup)/2);
            ordenar(a,(inf+sup)/2+1,sup);
            mezclar(a, inf, (inf+sup)/2, sup);
        }
    }
    private static ArrayList<MapNode> selectBestNodesMergeSort(ArrayList<MapNode> nodesList, int count) {
        ordenar(nodesList,0, nodesList.size()-1);
        ArrayList<MapNode> resultado = new ArrayList<>();

        for(int i = 0; i < count; i++){
            resultado.add(nodesList.get(i));
        }
        return resultado;
    }
    private static void intercambia(ArrayList<MapNode> a, int i , int j){
        MapNode aux = a.get(i);
        a.set(i,a.get(j));
        a.set(j, aux);
    }
    private static int partir(ArrayList<MapNode> a, int inf, int sup){
        float pivote = a.get(inf).getValue(0);
        int i = inf+1;
        int j = sup;

        do{
            while (i <= j && a.get(i).getValue(0) <= pivote){i++;}
            while (i <= j && a.get(j).getValue(0) > pivote){j--;}
            if(i<j){intercambia(a,i,j);}
        }while (i <= j);
        intercambia(a,inf,j);
        return j;
    }
    public static void ordenarQuick(ArrayList<MapNode> a, int inf, int sup){
        if(inf < sup){
            int p = partir(a, inf, sup);
            ordenarQuick(a, inf, p-1);
            ordenarQuick(a, p+1, sup);
        }
    }
    private static ArrayList<MapNode> selectBestNodesQuickSort(ArrayList<MapNode> nodesList, int count) {
        ArrayList<MapNode> minimos = new ArrayList<>();
        ordenarQuick(nodesList,0, nodesList.size()-1);

        for(int i = 0; i < count; i++){
            minimos.add(nodesList.get(i));
        }
        return minimos;
    }
}
