/*
Sustituir este comentario por una explicación de la formula o procedimiento empleado para determinar el valor de una
torreta.
*/

/*Nombre: Diego López Ruiz
* Para resolución del problema que se nos plantea en esta práctica he decidido utilizar un enfoque dinámico parecido al utilizado para la resolución del
* problema de la mochila (explicado en clase). Para ello me he creado mi matriz TSR con ArrayLists de tipo float para almacenar en sus celdas el valor que
* le asociaré a las torretas en función de sus características. Para el cálculo de dicho valor he realizado el siguiente cálculo: 100*tower.getDamage()-0.36f*tower.getCooldown()+0.6f*tower.getRange()-38.8f*tower.getDispersion()
* Con ese cálculo he conseguido una puntuación máxima de 18.*/

package net.agsh.towerdefense.strats;

import net.agsh.towerdefense.Config;
import net.agsh.towerdefense.Game;
import net.agsh.towerdefense.Tower;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

public class TowerBuyer {

    private static float getTowerValue(Tower tower)
    {
        Game g = Game.getInstance();

        float W_DAM = g.getParam(Config.Parameter.W_DAM);
        float W_CODW = g.getParam(Config.Parameter.W_CODW);
        float W_RAN = g.getParam(Config.Parameter.W_RAN);
        float W_DISP = g.getParam(Config.Parameter.W_DISP);
        return W_DAM*tower.getDamage()+W_CODW*tower.getCooldown()+W_RAN*tower.getRange()+W_DISP*tower.getDispersion(); //100;0.36;0.6;0.38.8
    }

    public static ArrayList<Integer> buyTowers(ArrayList<Tower> towers, float money) {
        ArrayList<Integer> selected = new ArrayList<>();

        // Me creo mi TSR
        ArrayList<ArrayList<Float>> TSR = new ArrayList<>();

        // Inicializo mi TSR
        for (int i = 0; i <= towers.size(); i++) {
            TSR.add(new ArrayList<>());
        }

        // Completo el resto de mi matriz TSR
        for (int i = 0; i <= towers.size(); i++) {
            for (int j = 0; j < (int) Math.ceil(money); j++) {
                //caso base
                if(i == 0 || j == 0){
                    TSR.get(i).add(0f);
                }else if(j < towers.get(i-1).getCost()) {
                    TSR.get(i).add(TSR.get(i-1).get(j));
                }else {
                    TSR.get(i).add(Math.max(TSR.get(i-1).get(j), TSR.get(i-1).get(j - (int) Math.ceil(towers.get(i-1).getCost())) + getTowerValue(towers.get(i-1))));
                }
            }
        }

        int i = towers.size();
        int j = (int) Math.ceil(money) - 1;
        while(i > 0 && j > 0){
            if(TSR.get(i).get(j).compareTo(TSR.get(i-1).get(j)) != 0 && towers.get(i-1).getCost() <= j){
                selected.add(i-1);
                j -= (int) Math.ceil(towers.get(i-1).getCost());
            }
            i--;
        }

        // Hice dos versiones del código para ver cuál me daba mejor resultado, pero al final me quedé con la primera.

        /*
        if(getTowerValue(towers.get(0)) >= 0 && j >= towers.get(0).getCost()){
            selected.add(i);
        }


        // The ArrayList<Integer> returned is a list of the indices of the towers you want to buy.
        // For example, if you want to buy the first and third towers, return [0, 2].
        // The selected towers must be affordable, and the total cost must be less than or equal to money.
        // The indices should be given in the order that the towers are given in the original ArrayList<Tower> towers.

        // Create an ArrayList<Integer> to store the indices of the towers you want to buy.

        // TSR
        int money_ceil = (int) Math.ceil(money);
        float[][] TSR = new float[towers.size()][money_ceil];

        // Relleno mi TSR
        for (int i = 0; i < towers.size(); i++){
            float tower_cost = towers.get(i).getCost();
            for(int j = 0; j < money_ceil; j++){
                if(i == 0 && j < tower_cost){ // Pongo un cero cuando el coste de la torreta supera el presupuesto máximo
                    TSR[i][j] = 0;
                }else if(i == 0 && j >= tower_cost){ // Pongo el valor asociado a la torreta cuando su coste no supera el máximo
                    TSR[i][j] = getTowerValue(towers.get(i));
                }else{
                    if(j < tower_cost){
                        TSR[i][j] = TSR[i-1][j];
                    }else{
                        TSR[i][j] = Math.max(TSR[i-1][j],TSR[i-1][j-(int)tower_cost]+getTowerValue(towers.get(i)));
                    }
                }
            }
        }


        // Extraigo el resultado final
        int i = towers.size()-1;
        int j = money_ceil-1;
        while(i > 0 && j > 0){
            if(TSR[i][j] != TSR[i-1][j] && towers.get(i).getCost() <= j){
                selected.add(i);
                j -= (int) Math.ceil(towers.get(i).getCost());
            }
            i--;
        }

        // Meto la última torreta si es que cabe
        if(getTowerValue(towers.get(0)) >= 0 && j >= towers.get(0).getCost()){
            selected.add(i);
        }

        */
        Collections.reverse(selected);

        return selected;
    }
}
