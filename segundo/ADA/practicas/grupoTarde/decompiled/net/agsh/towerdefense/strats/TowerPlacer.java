/*     */ package net.agsh.towerdefense.strats;
/*     */ import java.util.ArrayList;
/*     */ import net.agsh.towerdefense.Config;
/*     */ import net.agsh.towerdefense.Game;
/*     */ import net.agsh.towerdefense.MapNode;
/*     */ import net.agsh.towerdefense.Obstacle;
/*     */ import net.agsh.towerdefense.Point2D;
/*     */ import net.agsh.towerdefense.Tower;
/*     */ 
/*     */ public class TowerPlacer {
/*     */   static float getTowerValue(Tower tower) {
/*  12 */     Game g = Game.getInstance();
/*     */     
/*  14 */     float minRadius = g.getParam(Config.Parameter.TOWER_RADIUS_MIN);
/*  15 */     float maxRadius = g.getParam(Config.Parameter.TOWER_RADIUS_MAX);
/*  16 */     float normalizedRadius = (tower.getRadius() - minRadius) / (maxRadius - minRadius);
/*     */     
/*  18 */     float minDamage = g.getParam(Config.Parameter.TOWER_DAMAGE_MIN);
/*  19 */     float maxDamage = g.getParam(Config.Parameter.TOWER_DAMAGE_MAX);
/*  20 */     float normalizedDamage = (tower.getDamage() - minDamage) / (maxDamage - minDamage);
/*     */     
/*  22 */     float minRange = g.getParam(Config.Parameter.TOWER_RANGE_MIN);
/*  23 */     float maxRange = g.getParam(Config.Parameter.TOWER_RANGE_MAX);
/*  24 */     float normalizedRange = (tower.getRange() - minRange) / (maxRange - minRange);
/*     */     
/*  26 */     float minCooldown = g.getParam(Config.Parameter.TOWER_COOLDOWN_MIN);
/*  27 */     float maxCooldown = g.getParam(Config.Parameter.TOWER_COOLDOWN_MAX);
/*  28 */     float normalizedCooldown = (tower.getCooldown() - minCooldown) / (maxCooldown - minCooldown);
/*     */     
/*  30 */     float minDispersion = g.getParam(Config.Parameter.TOWER_DISPERSION_MIN);
/*  31 */     float maxDispersion = g.getParam(Config.Parameter.TOWER_DISPERSION_MAX);
/*  32 */     float normalizedDispersion = (tower.getDispersion() - minDispersion) / (maxDispersion - minDispersion);
/*     */     
/*  34 */     return -2.0F * normalizedRadius + 5.0F * normalizedDamage + 4.0F * normalizedRange + -1.0F * normalizedCooldown + 1.0F * normalizedDispersion;
/*     */   }
/*     */   static final int nodeValueIndex = 0;
/*     */   
/*     */   protected static boolean isInsideMap(Point2D position, float radius, Point2D mapSize) {
/*  39 */     if (position.x < radius || position.x > mapSize.x - radius || position.y < radius || position.y > mapSize.y - radius) {
/*  40 */       return false;
/*     */     }
/*  42 */     return true;
/*     */   }
/*     */   
/*     */   protected static boolean collidesWithObstacles(Point2D position, float radius, ArrayList<Obstacle> obstacles) {
/*  46 */     for (Obstacle o : obstacles) {
/*  47 */       if (position.distance(o.getPosition()) < radius + o.getRadius()) {
/*  48 */         return true;
/*     */       }
/*     */     } 
/*  51 */     return false;
/*     */   }
/*     */   
/*     */   protected static boolean collidesWithTowers(Point2D position, float radius, ArrayList<Tower> towers) {
/*  55 */     for (Tower t : towers) {
/*  56 */       if (position.distance(t.getPosition()) < radius + t.getRadius()) {
/*  57 */         return true;
/*     */       }
/*     */     } 
/*  60 */     return false;
/*     */   }
/*     */   
/*     */   protected static boolean collidesWithWalkableNodes(Point2D position, float radius, ArrayList<MapNode> walkableNodes) {
/*  64 */     for (MapNode n : walkableNodes) {
/*  65 */       if (position.distance(n.getPosition()) < radius) {
/*  66 */         return true;
/*     */       }
/*     */     } 
/*  69 */     return false;
/*     */   }
/*     */   
/*     */   static float getNodeValue(MapNode node) {
/*  73 */     float maxRange = Game.getInstance().getParam(Config.Parameter.TOWER_RANGE_MAX);
/*     */     
/*  75 */     int inRangeNodesCount = 0;
/*  76 */     for (MapNode n : Game.getInstance().getMap().getWalkableNodes()) {
/*  77 */       if (node.getPosition().distance(n.getPosition()) < maxRange) {
/*  78 */         inRangeNodesCount++;
/*     */       }
/*     */     } 
/*     */     
/*  82 */     return inRangeNodesCount;
/*     */   }
/*     */   
/*     */   public static ArrayList<Tower> placeTowers(ArrayList<Tower> towers, Map map) {
/*  86 */     Game g = Game.getInstance();
/*     */ 
/*     */     
/*  89 */     towers.sort((o1, o2) -> Float.compare(getTowerValue(o2), getTowerValue(o1)));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  96 */     ArrayList<WeighedMapNode> nodes = new ArrayList<>();
/*  97 */     MapNode[][] grid = map.getNodes();
/*  98 */     float minValue = Float.MAX_VALUE;
/*  99 */     float maxValue = Float.MIN_VALUE;
/* 100 */     for (MapNode[] mapNodes : grid) {
/* 101 */       for (MapNode n : mapNodes) {
/* 102 */         float weight = getNodeValue(n);
/* 103 */         n.setValue(0, weight);
/* 104 */         nodes.add(new WeighedMapNode(n, weight));
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 109 */     nodes.sort((o1, o2) -> Float.compare(o2.weight, o1.weight));
/*     */ 
/*     */     
/* 112 */     ArrayList<Tower> placedTowers = new ArrayList<>();
/* 113 */     float maxEnemyRadius = g.getParam(Config.Parameter.ENEMY_RADIUS_MAX);
/* 114 */     while (nodes.size() > 0 && placedTowers.size() < towers.size()) {
/* 115 */       WeighedMapNode node = nodes.get(0);
/* 116 */       nodes.remove(0);
/*     */       
/* 118 */       Point2D nodePosition = node.getMapNode().getPosition();
/* 119 */       float towerRadius = ((Tower)towers.get(0)).getRadius();
/* 120 */       if (isInsideMap(nodePosition, towerRadius, map.getSize()) && 
/* 121 */         !collidesWithObstacles(nodePosition, towerRadius, map.getObstacles()) && 
/* 122 */         !collidesWithTowers(nodePosition, towerRadius, placedTowers) && 
/* 123 */         !collidesWithWalkableNodes(nodePosition, towerRadius + maxEnemyRadius, g.getMap().getWalkableNodes())) {
/*     */         
/* 125 */         boolean valid = true;
/*     */         
/* 127 */         for (MapNode n : g.getMap().getWalkableNodes()) {
/* 128 */           if (valid && n.getPosition().distance(nodePosition) < (towerRadius + maxEnemyRadius) * 1.1F) {
/* 129 */             for (MapNode neighbor : n.getNeighbors()) {
/* 130 */               if (g.getMap().getWalkableNodes().contains(neighbor)) {
/* 131 */                 Point2D midPoint = neighbor.getPosition().midPoint(n.getPosition());
/* 132 */                 if (midPoint.distance(nodePosition) < towerRadius + maxEnemyRadius) {
/* 133 */                   valid = false;
/*     */                 }
/*     */               } 
/*     */             } 
/*     */           }
/*     */         } 
/*     */ 
/*     */         
/* 141 */         if (valid) {
/* 142 */           Tower tower = towers.get(0);
/* 143 */           towers.remove(0);
/*     */           
/* 145 */           tower.setPosition(node.getMapNode().getPosition());
/*     */           
/* 147 */           placedTowers.add(tower);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 152 */     return placedTowers;
/*     */   }
/*     */ }


/* Location:              /home/miguel/Universidad/segundo/ADA/practicas/grupoTarde/PracticaTarde3/towerdefense.jar!/net/agsh/towerdefense/strats/TowerPlacer.class
 * Java compiler version: 20 (64.0)
 * JD-Core Version:       1.1.3
 */