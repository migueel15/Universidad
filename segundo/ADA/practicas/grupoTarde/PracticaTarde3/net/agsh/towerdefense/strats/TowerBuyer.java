/*
Sustituir este comentario por una explicación de la formula o procedimiento empleado para determinar el valor de una
torreta.
*/

package net.agsh.towerdefense.strats;

//import net.agsh.towerdefense.Config;
import net.agsh.towerdefense.Config;
import net.agsh.towerdefense.Game;
import net.agsh.towerdefense.Tower;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/*
    En esta practica se utiliza una estrategia basada en programación dinámica para elegir con un presupuesto limitado en cada ronda la combinación de torretas mas óptima la cual
consiga que se nos escape el menor numero de goblins posibles dando lugar al score mas bajo. Para poder hacer la TSR(Tabla de subproblemas resueltos) necesitaba crear una funcion
que me evaluase las torretas objetivamente en función a sus atributos lo que ha dado lugar a la función “Valuetower” que en funcion de estos relativizandolos con respecto
a su máximo y multiplicandolos por una ponderación asigna un valor objetivo que indica la calidad de esa torreta. Dos problemas que he encontrado a la hora de realizar
la practica ha sido encontrar las ponderaciones correctas, aunque finalmente he concluido que el atributo mas importante es el daño, y en segundo lugar las limitaciones
que supone que solo haya 18 torretas ya que esto hace que no varíe mucho la puntuación al cambiar las ponderaciones puesto que la diferencia entre una solución óptima
y una no óptima al elegir 14 torretas de 18 no es lo mismo que al elegir 14 de 200, lo que me ha complicado llegar a la conclusión de que atributos son mas prioritarios,
incluso he podido apreciar que hay rondas en las que se eligen las 18 torretas y aun así entran goblins concluyendo en que es imposible conseguir una puntuación perfecta.
 */
public class TowerBuyer {

    /* Da 21 en normal
    private static final float WEIGHT_RANGE = 28f;
    private static final float WEIGHT_DAMAGE = 52f;
    private static final float WEIGHT_COOLDOWN = 4f;
    private static final float WEIGHT_DISPERSION = 29f;
     */
    private static final float WEIGHT_RANGE = 28f;
    private static final float WEIGHT_DAMAGE = 52f;
    private static final float WEIGHT_COOLDOWN = 4f;
    private static final float WEIGHT_DISPERSION = 29f;

    public static ArrayList<Integer> buyTowers(ArrayList<Tower> towers, float money) {
        // This is just a (bad) example. Replace ALL of this with your own code.
        // The ArrayList<Integer> returned is a list of the indices of the towers you want to buy.
        // For example, if you want to buy the first and third towers, return [0, 2].
        // The selected towers must be affordable, and the total cost must be less than or equal to money.
        // The indices should be given in the order that the towers are given in the original ArrayList<Tower> towers.

        // Creo un ArrayList<Integer> que guarda los indices de torretas que quiero comprar.
        ArrayList<Integer> selected = new ArrayList<>();
        int numTowers = towers.size();

        // Manejo el caso base donde no hay torretas
        if (numTowers == 0) {
            return new ArrayList<>();
        }

        //Miro los valores maximos de cada atributo de torretas para luego poder relativizarlos con respecto a estos
        float[][] dp = new float[numTowers + 1][(int) (money + 1)];

        towers.sort((o1, o2) -> {
            float v1 = o1.getCost();
            float v2 = o2.getCost();
            if (v1 > v2) {
                return -1;
            } else if (v1 < v2) {
                return 1;
            } else {
                return 0;
            }
        });
        // Lleno la tabla de manera iterativa
        for (int i = 0; i <= numTowers; i++) {
            for(int j = 0; j <= money ; j++){
                if(i==0 || j==0) {
                    dp[i][j]=0;
                }else if(i>0 && j<(int)Math.ceil(towers.get(i-1).getCost())){
                    dp[i][j]=dp[i-1][j];
                } else {
                    dp[i][j] = Math.max(dp[i-1][j],Valuetower(towers.get(i-1))+dp[i-1][j-(int)Math.ceil(towers.get(i-1).getCost())]);
                }
            }
        }

        // Recupero la solución óptima
        int i = numTowers;
        int j = (int) money;
        while (i > 0 && j > 0) {
            if (dp[i][j] != dp[i - 1][j]) {
                selected.add(i-1);
                j -= towers.get(i - 1).getCost();
            }
            i--;
        }

        Collections.reverse(selected);
        return selected;
    }
    public static float Valuetower(Tower tower) {
        Game g = Game.getInstance();
        float minDamage = g.getParam(Config.Parameter.TOWER_DAMAGE_MIN);
        float maxDamage = g.getParam(Config.Parameter.TOWER_DAMAGE_MAX);
        float damageScore = (tower.getDamage() - minDamage) / (maxDamage - minDamage);
        float minRange = g.getParam(Config.Parameter.TOWER_RANGE_MIN);
        float maxRange = g.getParam(Config.Parameter.TOWER_RANGE_MAX);
        float rangeScore = (tower.getRange() - minRange) / (maxRange - minRange);
        float minCooldown = g.getParam(Config.Parameter.TOWER_COOLDOWN_MIN);
        float maxCooldown = g.getParam(Config.Parameter.TOWER_COOLDOWN_MAX);
        float cooldownScore = (tower.getCooldown() - minCooldown) / (maxCooldown - minCooldown);
        float minDispersion = g.getParam(Config.Parameter.TOWER_DISPERSION_MIN);
        float maxDispersion = g.getParam(Config.Parameter.TOWER_DISPERSION_MAX);
        float dispersionScore = (tower.getDispersion() - minDispersion) / (maxDispersion - minDispersion);

        rangeScore = rangeScore * g.getParam(Config.Parameter.WEIGHT_RANGE);
        damageScore = damageScore * g.getParam(Config.Parameter.WEIGHT_DAMAGE);
        cooldownScore = (1 - cooldownScore) * g.getParam(Config.Parameter.WEIGHT_COOLDOWN); //
        dispersionScore = (dispersionScore) * g.getParam(Config.Parameter.WEIGHT_DISPERSION); //

        // Sumo los puntajes ponderados
        return rangeScore + damageScore + cooldownScore + dispersionScore;
    }}