/*    */ package net.agsh.towerdefense;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ 
/*    */ public class MapNode {
/*    */   Point2D position;
/*    */   ArrayList<MapNode> neighbors;
/*    */   boolean walkable = false;
/*    */   float[] values;
/*    */   
/*    */   public MapNode(Point2D position) {
/* 12 */     this.position = position;
/* 13 */     this.neighbors = new ArrayList<>();
/* 14 */     this.values = new float[(int)Game.getInstance().getParam(Config.Parameter.MAP_NODE_VALUES)];
/*    */   }
/*    */   
/*    */   public MapNode(Point2D position, boolean walkable, float[] values) {
/* 18 */     this.position = position;
/* 19 */     this.neighbors = new ArrayList<>();
/* 20 */     this.walkable = walkable;
/* 21 */     this.values = new float[(int)Game.getInstance().getParam(Config.Parameter.MAP_NODE_VALUES)];
/* 22 */     for (int i = 0; i < values.length; i++) {
/* 23 */       this.values[i] = values[i];
/*    */     }
/*    */   }
/*    */   
/*    */   public Point2D getPosition() {
/* 28 */     return this.position;
/*    */   }
/*    */   
/*    */   public ArrayList<MapNode> getNeighbors() {
/* 32 */     return this.neighbors;
/*    */   }
/*    */   
/*    */   public void addNeighbor(MapNode node) {
/* 36 */     this.neighbors.add(node);
/*    */   }
/*    */   
/*    */   public void removeNeighbor(MapNode node) {
/* 40 */     this.neighbors.remove(node);
/*    */   }
/*    */   
/*    */   public boolean isWalkable() {
/* 44 */     return this.walkable;
/*    */   }
/*    */   
/*    */   void setWalkable(boolean walkable) {
/* 48 */     this.walkable = walkable;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object obj) {
/* 53 */     if (obj instanceof MapNode) {
/* 54 */       MapNode node = (MapNode)obj;
/* 55 */       return node.getPosition().equals(this.position);
/*    */     } 
/* 57 */     return false;
/*    */   }
/*    */   
/*    */   public float getValue(int i) {
/* 61 */     return this.values[i];
/*    */   }
/*    */   
/*    */   public void setValue(int i, float value) {
/* 65 */     this.values[i] = value;
/*    */   }
/*    */   
/*    */   public float[] getValues() {
/* 69 */     return this.values;
/*    */   }
/*    */ }


/* Location:              /home/miguel/Universidad/segundo/ADA/practicas/grupoTarde/PracticaTarde3/towerdefense.jar!/net/agsh/towerdefense/MapNode.class
 * Java compiler version: 20 (64.0)
 * JD-Core Version:       1.1.3
 */