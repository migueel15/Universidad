/*    */ package net.agsh.towerdefense;
/*    */ 
/*    */ 
/*    */ public class Obstacle
/*    */   extends Entity
/*    */ {
/*    */   public Obstacle(float radius) {
/*  8 */     super(radius);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 13 */     return "Obstacle{id=" + this.id + " " + String.valueOf(this.position) + ", radius=" + this.radius + "}";
/*    */   }
/*    */ }


/* Location:              /home/miguel/Universidad/segundo/ADA/practicas/grupoTarde/PracticaTarde3/towerdefense.jar!/net/agsh/towerdefense/Obstacle.class
 * Java compiler version: 20 (64.0)
 * JD-Core Version:       1.1.3
 */