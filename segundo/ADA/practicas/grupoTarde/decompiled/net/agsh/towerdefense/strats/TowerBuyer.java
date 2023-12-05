/*    */ package net.agsh.towerdefense.strats;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import net.agsh.towerdefense.Config;
/*    */ import net.agsh.towerdefense.Game;
/*    */ import net.agsh.towerdefense.Tower;
/*    */ 
/*    */ 
/*    */ public class TowerBuyer
/*    */ {
/*    */   static float getTowerValue(Tower tower) {
/* 12 */     Game g = Game.getInstance();
/*    */     
/* 14 */     float minRadius = g.getParam(Config.Parameter.TOWER_RADIUS_MIN);
/* 15 */     float maxRadius = g.getParam(Config.Parameter.TOWER_RADIUS_MAX);
/* 16 */     float normalizedRadius = (tower.getRadius() - minRadius) / (maxRadius - minRadius);
/*    */     
/* 18 */     float minDamage = g.getParam(Config.Parameter.TOWER_DAMAGE_MIN);
/* 19 */     float maxDamage = g.getParam(Config.Parameter.TOWER_DAMAGE_MAX);
/* 20 */     float normalizedDamage = (tower.getDamage() - minDamage) / (maxDamage - minDamage);
/*    */     
/* 22 */     float minRange = g.getParam(Config.Parameter.TOWER_RANGE_MIN);
/* 23 */     float maxRange = g.getParam(Config.Parameter.TOWER_RANGE_MAX);
/* 24 */     float normalizedRange = (tower.getRange() - minRange) / (maxRange - minRange);
/*    */     
/* 26 */     float minCooldown = g.getParam(Config.Parameter.TOWER_COOLDOWN_MIN);
/* 27 */     float maxCooldown = g.getParam(Config.Parameter.TOWER_COOLDOWN_MAX);
/* 28 */     float normalizedCooldown = (tower.getCooldown() - minCooldown) / (maxCooldown - minCooldown);
/*    */     
/* 30 */     float minDispersion = g.getParam(Config.Parameter.TOWER_DISPERSION_MIN);
/* 31 */     float maxDispersion = g.getParam(Config.Parameter.TOWER_DISPERSION_MAX);
/* 32 */     float normalizedDispersion = (tower.getDispersion() - minDispersion) / (maxDispersion - minDispersion);
/*    */     
/* 34 */     return -2.0F * normalizedRadius + 5.0F * normalizedDamage + 4.0F * normalizedRange + -1.0F * normalizedCooldown + 1.0F * normalizedDispersion;
/*    */   }
/*    */ 
/*    */   
/*    */   private static int getTowerCost(Tower tower) {
/* 39 */     return (int)Math.ceil(tower.getCost());
/*    */   }
/*    */ 
/*    */   
/*    */   public static ArrayList<Integer> buyTowers(ArrayList<Tower> towers, float money) {
/* 44 */     float[][] dp = new float[towers.size()][(int)money + 1];
/* 45 */     for (int i = 0; i < towers.size(); i++) {
/* 46 */       for (int m = 0; m <= money; m++) {
/* 47 */         int towerCost = getTowerCost(towers.get(i));
/* 48 */         if (m == 0) {
/* 49 */           dp[i][m] = 0.0F;
/* 50 */         } else if (i == 0) {
/* 51 */           if (m >= towerCost) {
/* 52 */             dp[i][m] = getTowerValue((Tower)towers.get(i));
/*    */           } else {
/* 54 */             dp[i][m] = 0.0F;
/*    */           } 
/* 56 */         } else if (towerCost <= m) {
/* 57 */           dp[i][m] = Math.max(dp[i - 1][m], 
/*    */               
/* 59 */               getTowerValue((Tower)towers.get(i)) + dp[i - 1][m - towerCost]);
/*    */         } else {
/* 61 */           dp[i][m] = dp[i - 1][m];
/*    */         } 
/*    */       } 
/*    */     } 
/*    */     
/* 66 */     ArrayList<Integer> selected = new ArrayList<>();
/* 67 */     int k = dp.length - 1;
/* 68 */     int j = (dp[0]).length - 1;
/* 69 */     while (k > 0 && j > 0) {
/* 70 */       if (dp[k][j] != dp[k - 1][j]) {
/* 71 */         selected.add(Integer.valueOf(k));
/* 72 */         j -= getTowerCost(towers.get(k));
/*    */       } 
/* 74 */       k--;
/*    */     } 
/* 76 */     if (k == 0 && dp[k][j] != 0.0F) {
/* 77 */       selected.add(Integer.valueOf(k));
/*    */     }
/*    */     
/* 80 */     return selected;
/*    */   }
/*    */ }


/* Location:              /home/miguel/Universidad/segundo/ADA/practicas/grupoTarde/PracticaTarde3/towerdefense.jar!/net/agsh/towerdefense/strats/TowerBuyer.class
 * Java compiler version: 20 (64.0)
 * JD-Core Version:       1.1.3
 */