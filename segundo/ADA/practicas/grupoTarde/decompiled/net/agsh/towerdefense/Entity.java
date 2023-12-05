/*    */ package net.agsh.towerdefense;
/*    */ 
/*    */ public class Entity {
/*  4 */   static int nextId = 0;
/*    */   int id;
/*    */   Point2D position;
/*    */   float radius;
/*    */   
/*    */   public Entity(float radius) {
/* 10 */     this.id = getNextId();
/* 11 */     this.radius = radius;
/*    */   }
/*    */   
/*    */   public int getId() {
/* 15 */     return this.id;
/*    */   }
/*    */   
/*    */   private static int getNextId() {
/* 19 */     return nextId++;
/*    */   }
/*    */   
/*    */   public void setPosition(Point2D position) {
/* 23 */     this.position = position;
/*    */   }
/*    */   
/*    */   public Point2D getPosition() {
/* 27 */     return this.position;
/*    */   }
/*    */   public float getRadius() {
/* 30 */     return this.radius;
/*    */   }
/*    */ }


/* Location:              /home/miguel/Universidad/segundo/ADA/practicas/grupoTarde/PracticaTarde3/towerdefense.jar!/net/agsh/towerdefense/Entity.class
 * Java compiler version: 20 (64.0)
 * JD-Core Version:       1.1.3
 */