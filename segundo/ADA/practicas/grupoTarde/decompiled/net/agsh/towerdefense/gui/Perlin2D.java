/*    */ package net.agsh.towerdefense.gui;
/*    */ 
/*    */ import java.util.Random;
/*    */ 
/*    */ public class Perlin2D
/*    */ {
/*    */   private int[] permutation;
/*    */   
/*    */   public Perlin2D() {
/* 10 */     Random rand = new Random();
/*    */ 
/*    */     
/* 13 */     this.permutation = new int[512]; int i;
/* 14 */     for (i = 0; i < 256; i++) {
/* 15 */       this.permutation[i] = i;
/*    */     }
/*    */     
/* 18 */     for (i = 0; i < 256; i++) {
/* 19 */       int j = rand.nextInt(256 - i) + i;
/* 20 */       int temp = this.permutation[i];
/* 21 */       this.permutation[i] = this.permutation[j];
/* 22 */       this.permutation[j] = temp;
/* 23 */       this.permutation[i + 256] = this.permutation[i];
/*    */     } 
/*    */   }
/*    */   
/*    */   private double fade(double t) {
/* 28 */     return t * t * t * (t * (t * 6.0D - 15.0D) + 10.0D);
/*    */   }
/*    */   
/*    */   private double lerp(double t, double a, double b) {
/* 32 */     return a + t * (b - a);
/*    */   }
/*    */   
/*    */   private double grad(int hash, double x, double y) {
/* 36 */     int h = hash & 0xF;
/* 37 */     double u = (h < 8) ? x : y;
/* 38 */     double v = (h < 4) ? y : ((h == 12 || h == 14) ? x : 0.0D);
/* 39 */     return (((h & 0x1) == 0) ? u : -u) + (((h & 0x2) == 0) ? v : -v);
/*    */   }
/*    */   
/*    */   public double noise(double x, double y) {
/* 43 */     int X = (int)Math.floor(x) & 0xFF;
/* 44 */     int Y = (int)Math.floor(y) & 0xFF;
/*    */     
/* 46 */     x -= Math.floor(x);
/* 47 */     y -= Math.floor(y);
/*    */     
/* 49 */     double u = fade(x);
/* 50 */     double v = fade(y);
/*    */     
/* 52 */     int A = this.permutation[X] + Y;
/* 53 */     int B = this.permutation[X + 1] + Y;
/*    */     
/* 55 */     return lerp(v, lerp(u, grad(this.permutation[A], x, y), 
/* 56 */           grad(this.permutation[B], x - 1.0D, y)), 
/* 57 */         lerp(u, grad(this.permutation[A + 1], x, y - 1.0D), 
/* 58 */           grad(this.permutation[B + 1], x - 1.0D, y - 1.0D)));
/*    */   }
/*    */ }


/* Location:              /home/miguel/Universidad/segundo/ADA/practicas/grupoTarde/PracticaTarde3/towerdefense.jar!/net/agsh/towerdefense/gui/Perlin2D.class
 * Java compiler version: 20 (64.0)
 * JD-Core Version:       1.1.3
 */