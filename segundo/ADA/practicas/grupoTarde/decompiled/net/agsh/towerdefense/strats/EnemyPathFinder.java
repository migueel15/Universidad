/*     */ package net.agsh.towerdefense.strats;
/*     */ import java.util.ArrayList;
/*     */ import net.agsh.towerdefense.Game;
/*     */ import net.agsh.towerdefense.Map;
/*     */ import net.agsh.towerdefense.MapNode;
/*     */ import net.agsh.towerdefense.Obstacle;
/*     */ import net.agsh.towerdefense.Point2D;
/*     */ import net.agsh.towerdefense.Tower;
/*     */ 
/*     */ public class EnemyPathFinder {
/*     */   protected static boolean collidesWithObstacles(Point2D position, float radius, ArrayList<Obstacle> obstacles) {
/*  12 */     for (Obstacle o : obstacles) {
/*  13 */       if (position.distance(o.getPosition()) < radius + o.getRadius()) {
/*  14 */         return true;
/*     */       }
/*     */     } 
/*  17 */     return false;
/*     */   }
/*     */   static final int nodeValueIndex = 1;
/*     */   protected static boolean collidesWithTowers(Point2D position, float radius, ArrayList<Tower> towers) {
/*  21 */     for (Tower t : towers) {
/*  22 */       if (position.distance(t.getPosition()) < radius + t.getRadius()) {
/*  23 */         return true;
/*     */       }
/*     */     } 
/*  26 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public static ArrayList<MapNode> findBestPath(MapNode position, ArrayList<MapNode> walkableNodes, ArrayList<MapNode> endingPoints) {
/*  31 */     Game g = Game.getInstance();
/*  32 */     Map m = g.getMap();
/*  33 */     ArrayList<ArrayList<MapNode>> paths = new ArrayList<>();
/*  34 */     for (MapNode p : m.getEndingPoints()) {
/*  35 */       ArrayList<MapNode> path = findPath(position, p, walkableNodes);
/*  36 */       if (path != null) {
/*  37 */         paths.add(path);
/*     */       }
/*     */     } 
/*     */     
/*  41 */     float minDistance = Float.MAX_VALUE;
/*  42 */     ArrayList<MapNode> minPath = null;
/*  43 */     for (ArrayList<MapNode> path : paths) {
/*  44 */       float distance = 0.0F;
/*  45 */       for (int i = 0; i < path.size() - 1; i++) {
/*  46 */         distance += ((MapNode)path.get(i)).getPosition().distance(((MapNode)path.get(i + 1)).getPosition());
/*     */       }
/*  48 */       if (distance < minDistance) {
/*  49 */         minDistance = distance;
/*  50 */         minPath = path;
/*     */       } 
/*     */     } 
/*     */     
/*  54 */     return minPath;
/*     */   }
/*     */   
/*     */   static ArrayList<MapNode> findPath(MapNode origin, MapNode destination, ArrayList<MapNode> walkableNodes) {
/*  58 */     Game g = Game.getInstance();
/*  59 */     Map m = g.getMap();
/*     */ 
/*     */     
/*  62 */     for (MapNode[] nodes : m.getNodes()) {
/*  63 */       for (MapNode node : nodes) {
/*  64 */         node.setValue(1, -1.0F);
/*     */       }
/*     */     } 
/*     */     
/*  68 */     float maxEnemyRadius = g.getParam(Config.Parameter.ENEMY_RADIUS_MAX);
/*     */     
/*  70 */     ArrayList<AStarNode> open = new ArrayList<>();
/*  71 */     ArrayList<AStarNode> closed = new ArrayList<>();
/*     */     
/*  73 */     AStarNode current = new AStarNode(origin);
/*  74 */     current.setG(0.0F);
/*  75 */     current.setH(current.mapNode.getPosition().distance(destination.getPosition()));
/*  76 */     open.add(current);
/*     */     
/*  78 */     while (!open.isEmpty()) {
/*  79 */       current = open.get(0);
/*  80 */       for (AStarNode n : open) {
/*  81 */         if (n.getF() < current.getF()) {
/*  82 */           current = n;
/*     */         }
/*     */       } 
/*  85 */       open.remove(current);
/*  86 */       closed.add(current);
/*     */       
/*  88 */       if (current.getMapNode() == destination) {
/*  89 */         ArrayList<MapNode> path = new ArrayList<>();
/*  90 */         while (current != null) {
/*  91 */           path.add(0, current.getMapNode());
/*  92 */           current = current.getParent();
/*     */         } 
/*  94 */         return path;
/*     */       } 
/*     */       
/*  97 */       for (MapNode n : current.getMapNode().getNeighbors()) {
/*  98 */         if (walkableNodes.contains(n)) {
/*  99 */           AStarNode neighbor = new AStarNode(n);
/* 100 */           if (!closed.contains(neighbor)) {
/* 101 */             if (!open.contains(neighbor)) {
/* 102 */               Point2D midPoint = current.getMapNode().getPosition().midPoint(n.getPosition());
/* 103 */               if ((m.getObstacles() == null || !collidesWithObstacles(midPoint, maxEnemyRadius, m.getObstacles())) && (m
/* 104 */                 .getTowers() == null || !collidesWithTowers(midPoint, maxEnemyRadius, m.getTowers()))) {
/*     */                 
/* 106 */                 neighbor.setG(current.getG() + current.getMapNode().getPosition().distance(n.getPosition()) * getPenalty(n));
/* 107 */                 neighbor.setH(calcH(n, destination));
/* 108 */                 neighbor.setParent(current);
/*     */                 
/* 110 */                 open.add(neighbor);
/* 111 */                 neighbor.getMapNode().setValue(1, neighbor.getF());
/*     */               } 
/*     */             }  continue;
/*     */           } 
/* 115 */           neighbor.setG(current.getG() + current.getMapNode().getPosition().distance(n.getPosition()));
/* 116 */           neighbor.setH(calcH(n, destination));
/* 117 */           neighbor.setParent(current);
/*     */ 
/*     */           
/* 120 */           AStarNode closedNeighbor = closed.get(closed.indexOf(neighbor));
/* 121 */           if (neighbor.getG() < closedNeighbor.getG()) {
/* 122 */             closed.remove(closedNeighbor);
/* 123 */             neighbor.setParent(current);
/* 124 */             open.add(neighbor);
/* 125 */             neighbor.getMapNode().setValue(1, neighbor.getF());
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 132 */     return null;
/*     */   }
/*     */   
/*     */   private static float calcH(MapNode n, MapNode destination) {
/* 136 */     float distanceFactor = 0.5F;
/* 137 */     float damagePerSecondFactor = 0.5F;
/*     */ 
/*     */     
/* 140 */     float damagePerSecond = 0.0F;
/* 141 */     ArrayList<Tower> towers = Game.getInstance().getMap().getTowers();
/* 142 */     for (Tower t : towers) {
/* 143 */       if (t.getPosition().distance(n.getPosition()) < t.getRange()) {
/* 144 */         damagePerSecond += t.getDamage() / t.getCooldown();
/*     */       }
/*     */     } 
/*     */     
/* 148 */     return n.getPosition().distance(destination.getPosition()) * distanceFactor + damagePerSecond * damagePerSecondFactor;
/*     */   }
/*     */ 
/*     */   
/*     */   private static float getPenalty(MapNode n) {
/* 153 */     ArrayList<Tower> towers = Game.getInstance().getMap().getTowers();
/* 154 */     float penalty = 1.0F;
/* 155 */     for (Tower t : towers) {
/* 156 */       if (t.getPosition().distance(n.getPosition()) < t.getRange()) {
/* 157 */         penalty *= 1.1F;
/*     */       }
/*     */     } 
/*     */     
/* 161 */     return penalty;
/*     */   }
/*     */ }


/* Location:              /home/miguel/Universidad/segundo/ADA/practicas/grupoTarde/PracticaTarde3/towerdefense.jar!/net/agsh/towerdefense/strats/EnemyPathFinder.class
 * Java compiler version: 20 (64.0)
 * JD-Core Version:       1.1.3
 */