/*     */ package net.agsh.towerdefense;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ 
/*     */ public class Tower extends Entity {
/*   6 */   Point2D target = null; float range;
/*     */   float damage;
/*   8 */   long lastShotTime = 0L; float cooldown; float dispersion; float cost;
/*     */   
/*     */   public Tower() {
/*  11 */     super(Game.getInstance().getRandom().nextFloat(
/*  12 */           Game.getInstance().getParam(Config.Parameter.TOWER_RADIUS_MIN), 
/*  13 */           Game.getInstance().getParam(Config.Parameter.TOWER_RADIUS_MAX)));
/*     */     
/*  15 */     Game g = Game.getInstance();
/*  16 */     Random r = Game.getInstance().getRandom();
/*     */     
/*  18 */     float minRange = 2.0F * getRadius() + g.getParam(Config.Parameter.ENEMY_RADIUS_MIN);
/*  19 */     this.range = r.nextFloat(minRange, Math.max(minRange, g.getParam(Config.Parameter.TOWER_RANGE_MAX)));
/*  20 */     this.damage = r.nextFloat(g.getParam(Config.Parameter.TOWER_DAMAGE_MIN), g.getParam(Config.Parameter.TOWER_DAMAGE_MAX));
/*  21 */     this.cooldown = r.nextFloat(g.getParam(Config.Parameter.TOWER_COOLDOWN_MIN), g.getParam(Config.Parameter.TOWER_COOLDOWN_MAX));
/*  22 */     this.dispersion = r.nextFloat(g.getParam(Config.Parameter.TOWER_DISPERSION_MIN), g.getParam(Config.Parameter.TOWER_DISPERSION_MAX));
/*  23 */     this.cost = r.nextFloat(g.getParam(Config.Parameter.TOWER_COST_MIN), g.getParam(Config.Parameter.TOWER_COST_MAX));
/*     */   }
/*     */   
/*     */   public float getCost() {
/*  27 */     return this.cost;
/*     */   }
/*     */   
/*     */   public float getRange() {
/*  31 */     return this.range;
/*     */   }
/*     */   
/*     */   public float getCooldown() {
/*  35 */     return this.cooldown;
/*     */   }
/*     */   
/*     */   public float getDamage() {
/*  39 */     return this.damage;
/*     */   }
/*     */   public float getDispersion() {
/*  42 */     return this.dispersion;
/*     */   }
/*     */   public Point2D getTarget() {
/*  45 */     return this.target;
/*     */   }
/*     */   
/*     */   public float getCooldownLeft() {
/*  49 */     int timeSinceLastShot = (int)(Game.getInstance().getCurrentRound().getElapsedTime() - this.lastShotTime);
/*  50 */     return Math.max(0.0F, this.cooldown - timeSinceLastShot);
/*     */   }
/*     */   
/*     */   public void setTarget(Point2D target) {
/*  54 */     this.target = target;
/*     */   }
/*     */   
/*     */   private void shoot() {
/*  58 */     Game g = Game.getInstance();
/*  59 */     this.lastShotTime = g.getCurrentRound().getElapsedTime();
/*  60 */     for (Enemy e : g.getCurrentRound().getAliveEnemies()) {
/*  61 */       if (e.getPosition().distance(this.target) < this.dispersion) {
/*  62 */         float distanceToImpact = e.getPosition().distance(this.target);
/*  63 */         float normalizedDistance = distanceToImpact / this.dispersion;
/*  64 */         e.damage((1.0F - normalizedDistance) * this.damage);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private Point2D searchTarget(ArrayList<Enemy> enemiesInRange) {
/*  70 */     Game g = Game.getInstance();
/*  71 */     Enemy closest = null;
/*  72 */     for (Enemy e : enemiesInRange) {
/*  73 */       if (e.getPosition().distance(this.position) < this.range && (
/*  74 */         closest == null || e.getPosition().distance(this.position) < closest.getPosition().distance(this.position))) {
/*  75 */         closest = e;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/*  80 */     if (closest != null) {
/*  81 */       return closest.getPosition();
/*     */     }
/*  83 */     return null;
/*     */   }
/*     */   
/*     */   public void update(long millis) {
/*  87 */     Game g = Game.getInstance();
/*     */     
/*  89 */     ArrayList<Enemy> enemiesInRange = g.getMap().getEnemiesInRange(this.position, this.range);
/*     */     
/*  91 */     if (enemiesInRange.size() > 0 && 
/*  92 */       getCooldownLeft() == 0.0F) {
/*  93 */       this.target = searchTarget(enemiesInRange);
/*  94 */       shoot();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Tower clone() {
/* 100 */     Tower t = new Tower();
/* 101 */     t.position = this.position;
/* 102 */     t.target = this.target;
/* 103 */     t.range = this.range;
/* 104 */     t.damage = this.damage;
/* 105 */     t.cooldown = this.cooldown;
/* 106 */     t.dispersion = this.dispersion;
/* 107 */     t.cost = this.cost;
/* 108 */     return t;
/*     */   }
/*     */   
/*     */   public boolean equals(Object o) {
/* 112 */     if (o instanceof Tower) {
/* 113 */       Tower t = (Tower)o;
/* 114 */       return (t.position.equals(this.position) && t.target
/* 115 */         .equals(this.target) && t.range == this.range && t.damage == this.damage && t.cooldown == this.cooldown && t.dispersion == this.dispersion && t.cost == this.cost);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 122 */     return false;
/*     */   }
/*     */   
/*     */   public boolean sameTypeAs(Tower t) {
/* 126 */     return (t.range == this.range && t.damage == this.damage && t.cooldown == this.cooldown && t.dispersion == this.dispersion && t.cost == this.cost);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 137 */     return "Tower{id=" + this.id + " " + String.valueOf(this.position) + ", radius=" + this.radius + ", cost=" + this.cost + ", damage=" + this.damage + ", range=" + this.range + ", cooldown=" + this.cooldown + ", dispersion=" + this.dispersion + "}";
/*     */   }
/*     */ }


/* Location:              /home/miguel/Universidad/segundo/ADA/practicas/grupoTarde/PracticaTarde3/towerdefense.jar!/net/agsh/towerdefense/Tower.class
 * Java compiler version: 20 (64.0)
 * JD-Core Version:       1.1.3
 */