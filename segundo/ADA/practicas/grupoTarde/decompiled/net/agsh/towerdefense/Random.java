/*    */ package net.agsh.towerdefense;
/*    */ 
/*    */ import java.util.ArrayDeque;
/*    */ 
/*    */ public class Random {
/*    */   private final java.util.Random random;
/*  7 */   private final ArrayDeque<Long> seeds = new ArrayDeque<>();
/*    */   
/*    */   public Random() {
/* 10 */     this.random = new java.util.Random();
/*    */   }
/*    */   
/*    */   public Random(long seed) {
/* 14 */     this.random = new java.util.Random(seed);
/*    */   }
/*    */   public void setSeed(long seed) {
/* 17 */     this.random.setSeed(seed);
/*    */   }
/*    */   public float nextFloat(float min, float max) {
/* 20 */     return min + this.random.nextFloat() * (max - min);
/*    */   }
/*    */   
/*    */   public float nextFloat(float bound) {
/* 24 */     return this.random.nextFloat() * bound;
/*    */   }
/*    */   
/*    */   public int nextInt(int min, int max) {
/* 28 */     return min + this.random.nextInt(max - min);
/*    */   }
/*    */   
/*    */   public int nextInt(int bound) {
/* 32 */     return this.random.nextInt(bound);
/*    */   }
/*    */   
/*    */   public boolean nextBoolean() {
/* 36 */     return this.random.nextBoolean();
/*    */   }
/*    */   
/*    */   public double nextDouble() {
/* 40 */     return this.random.nextDouble();
/*    */   }
/*    */   public float nextFloat() {
/* 43 */     return this.random.nextFloat();
/*    */   } public long nextLong() {
/* 45 */     return this.random.nextLong();
/*    */   } public void pushSeed() {
/* 47 */     this.seeds.push(Long.valueOf(this.random.nextLong()));
/*    */   } public void popSeed() {
/* 49 */     this.random.setSeed(((Long)this.seeds.pop()).longValue());
/*    */   }
/*    */ }


/* Location:              /home/miguel/Universidad/segundo/ADA/practicas/grupoTarde/PracticaTarde3/towerdefense.jar!/net/agsh/towerdefense/Random.class
 * Java compiler version: 20 (64.0)
 * JD-Core Version:       1.1.3
 */