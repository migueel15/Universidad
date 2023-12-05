/*    */ package net.agsh.towerdefense;
/*    */ public class Point2D {
/*    */   public float x;
/*    */   
/*    */   public Point2D(float x, float y) {
/*  6 */     this.x = x;
/*  7 */     this.y = y;
/*    */   }
/*    */   public float y;
/*    */   public Point2D(Point2D p) {
/* 11 */     this.x = p.x;
/* 12 */     this.y = p.y;
/*    */   }
/*    */   
/*    */   public Point2D() {
/* 16 */     this.x = 0.0F;
/* 17 */     this.y = 0.0F;
/*    */   }
/*    */   
/*    */   public Point2D add(Point2D p) {
/* 21 */     return new Point2D(this.x + p.x, this.y + p.y);
/*    */   }
/*    */   
/*    */   public Point2D sub(Point2D p) {
/* 25 */     return new Point2D(this.x - p.x, this.y - p.y);
/*    */   }
/*    */   
/*    */   public Point2D mul(float i) {
/* 29 */     return new Point2D(this.x * i, this.y * i);
/*    */   }
/*    */   
/*    */   public Point2D div(float i) {
/* 33 */     return new Point2D(this.x / i, this.y / i);
/*    */   }
/*    */   
/*    */   public float length() {
/* 37 */     return (float)Math.sqrt((this.x * this.x + this.y * this.y));
/*    */   }
/*    */   
/*    */   public Point2D normalize() {
/* 41 */     return div(length());
/*    */   }
/*    */   
/*    */   public Point2D rotate(double angle) {
/* 45 */     double cos = Math.cos(angle);
/* 46 */     double sin = Math.sin(angle);
/* 47 */     return new Point2D((int)(this.x * cos - this.y * sin), (int)(this.x * sin + this.y * cos));
/*    */   }
/*    */   
/*    */   public double angle() {
/* 51 */     return Math.atan2(this.y, this.x);
/*    */   }
/*    */   
/*    */   public float distance(Point2D p) {
/* 55 */     return sub(p).length();
/*    */   }
/*    */   
/*    */   public float squaredDistance(Point2D p) {
/* 59 */     Point2D d = sub(p);
/* 60 */     return d.x * d.x + d.y * d.y;
/*    */   }
/*    */   
/*    */   public float manhattanDistance(Point2D p) {
/* 64 */     return Math.abs(this.x - p.x) + Math.abs(this.y - p.y);
/*    */   }
/*    */   
/*    */   public Point2D midPoint(Point2D p) {
/* 68 */     return new Point2D((this.x + p.x) / 2.0F, (this.y + p.y) / 2.0F);
/*    */   }
/*    */   
/*    */   public Point2D clone() {
/* 72 */     return new Point2D(this);
/*    */   }
/*    */   
/*    */   public String toString() {
/* 76 */     return "<" + this.x + ", " + this.y + ">";
/*    */   }
/*    */   
/*    */   public boolean equals(Point2D p) {
/* 80 */     return (this.x == p.x && this.y == p.y);
/*    */   }
/*    */   
/*    */   public boolean equals(Point2D p, float margin) {
/* 84 */     return (p.distance(this) <= margin);
/*    */   }
/*    */ }


/* Location:              /home/miguel/Universidad/segundo/ADA/practicas/grupoTarde/PracticaTarde3/towerdefense.jar!/net/agsh/towerdefense/Point2D.class
 * Java compiler version: 20 (64.0)
 * JD-Core Version:       1.1.3
 */