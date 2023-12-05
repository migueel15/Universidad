/*     */ package net.agsh.towerdefense.strats;
/*     */ 
/*     */ import net.agsh.towerdefense.MapNode;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class AStarNode
/*     */ {
/*     */   AStarNode parent;
/*     */   MapNode mapNode;
/*     */   float g;
/*     */   float h;
/*     */   
/*     */   public AStarNode(MapNode mapNode) {
/* 171 */     this.mapNode = mapNode;
/*     */   }
/*     */   
/*     */   public float getF() {
/* 175 */     return this.g + this.h;
/*     */   }
/*     */   
/*     */   public void setParent(AStarNode parent) {
/* 179 */     this.parent = parent;
/*     */   }
/*     */   
/*     */   public AStarNode getParent() {
/* 183 */     return this.parent;
/*     */   }
/*     */   
/*     */   public void setG(float g) {
/* 187 */     this.g = g;
/*     */   }
/*     */   
/*     */   public float getG() {
/* 191 */     return this.g;
/*     */   }
/*     */   
/*     */   public void setH(float h) {
/* 195 */     this.h = h;
/*     */   }
/*     */   
/*     */   public float getH() {
/* 199 */     return this.h;
/*     */   }
/*     */   
/*     */   public MapNode getMapNode() {
/* 203 */     return this.mapNode;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 208 */     if (o instanceof AStarNode) {
/* 209 */       return this.mapNode.equals(((AStarNode)o).getMapNode());
/*     */     }
/* 211 */     return false;
/*     */   }
/*     */ }


/* Location:              /home/miguel/Universidad/segundo/ADA/practicas/grupoTarde/PracticaTarde3/towerdefense.jar!/net/agsh/towerdefense/strats/AStarNode.class
 * Java compiler version: 20 (64.0)
 * JD-Core Version:       1.1.3
 */