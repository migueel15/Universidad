/*     */ package net.agsh.towerdefense;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import net.agsh.towerdefense.strats.EnemyPathFinder;
/*     */ 
/*     */ public class Enemy
/*     */   extends Entity {
/*   8 */   MapNode nextPosition = null;
/*   9 */   float speed = 0.0F; float health = 0.0F; float maxHealth = 0.0F;
/*  10 */   ArrayList<MapNode> path = null;
/*     */   
/*     */   public Enemy() {
/*  13 */     super(Game.getInstance().getRandom().nextFloat(
/*  14 */           Game.getInstance().getParam(Config.Parameter.ENEMY_RADIUS_MIN), 
/*  15 */           Game.getInstance().getParam(Config.Parameter.ENEMY_RADIUS_MAX)));
/*     */     
/*  17 */     Game g = Game.getInstance();
/*  18 */     Random r = Game.getInstance().getRandom();
/*     */     
/*  20 */     this.health = r.nextFloat(g.getParam(Config.Parameter.ENEMY_HEALTH_MIN), g.getParam(Config.Parameter.ENEMY_HEALTH_MAX));
/*  21 */     this.maxHealth = this.health;
/*     */     
/*  23 */     if (g.getParam(Config.Parameter.ENEMY_RADIUS_MIN) != g.getParam(Config.Parameter.ENEMY_RADIUS_MAX)) {
/*  24 */       this.radius = r.nextFloat(g.getParam(Config.Parameter.ENEMY_RADIUS_MIN), g.getParam(Config.Parameter.ENEMY_RADIUS_MAX));
/*     */     } else {
/*  26 */       this.radius = g.getParam(Config.Parameter.ENEMY_RADIUS_MIN);
/*     */     } 
/*  28 */     this.speed = r.nextFloat(g.getParam(Config.Parameter.ENEMY_SPEED_MIN), g.getParam(Config.Parameter.ENEMY_SPEED_MAX));
/*     */   }
/*     */   
/*     */   public void update(long millis) {
/*  32 */     Map m = Game.getInstance().getMap();
/*     */ 
/*     */     
/*  35 */     if (this.nextPosition == null) {
/*  36 */       this.path = EnemyPathFinder.findBestPath(m.nearestMapNode(this.position), m
/*  37 */           .getWalkableNodes(), m.getEndingPoints());
/*  38 */       if (this.path != null) {
/*  39 */         this.nextPosition = this.path.get(0);
/*     */       } else {
/*     */         
/*  42 */         System.out.println("ERROR: enemy " + this.id + " could not find valid path");
/*  43 */         this.health = 0.0F;
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
/*  48 */     float distanceToNextPosition = this.nextPosition.getPosition().distance(this.position);
/*     */ 
/*     */     
/*  51 */     while (this.nextPosition != null && distanceToNextPosition < 0.1F) {
/*  52 */       if (this.path.size() > 0) {
/*  53 */         this.path.remove(0);
/*  54 */         this.nextPosition = this.path.get(0);
/*  55 */         distanceToNextPosition = this.nextPosition.getPosition().distance(this.position); continue;
/*     */       } 
/*  57 */       this.nextPosition = null;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*  62 */     if (this.nextPosition != null) {
/*  63 */       Point2D direction = this.nextPosition.getPosition().sub(this.position);
/*  64 */       direction = direction.normalize();
/*  65 */       direction = direction.mul(Math.min(this.speed * (float)millis / 1000.0F, distanceToNextPosition));
/*  66 */       this.position = this.position.add(direction);
/*     */       
/*  68 */       if (m.collidesWithObstacles(this.position, this.radius, m.getObstacles()) || m
/*  69 */         .collidesWithTowers(this.position, this.radius, m.getTowers())) {
/*  70 */         System.out.println("ERROR: enemy " + this.id + " collided with obstacle or tower at " + String.valueOf(this.position));
/*  71 */         this.health = 0.0F;
/*     */         
/*     */         return;
/*     */       } 
/*  75 */       if (!m.nearestMapNode(this.position).isWalkable()) {
/*  76 */         System.out.println("ERROR: enemy " + this.id + " is not on a walkable node at " + String.valueOf(this.position));
/*  77 */         this.health = 0.0F;
/*     */         return;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public ArrayList<MapNode> getPath() {
/*  84 */     return this.path;
/*     */   }
/*     */   
/*     */   public boolean isDead() {
/*  88 */     return (this.health <= 0.0F);
/*     */   }
/*     */   
/*     */   public boolean hasEscaped() {
/*  92 */     Game g = Game.getInstance();
/*  93 */     for (MapNode p : g.getMap().getEndingPoints()) {
/*  94 */       if (p.getPosition().distance(this.position) < 1.0F) {
/*  95 */         return true;
/*     */       }
/*     */     } 
/*     */     
/*  99 */     return false;
/*     */   }
/*     */   
/*     */   public void damage(float damage) {
/* 103 */     this.health -= damage;
/*     */   }
/*     */   
/*     */   public float getHealth() {
/* 107 */     return this.health;
/*     */   }
/*     */   
/*     */   public float getMaxHealth() {
/* 111 */     return this.maxHealth;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 116 */     return "Enemy{id=" + this.id + " " + String.valueOf(this.position) + ", radius=" + this.radius + ", speed=" + this.speed + ", health=" + this.health + ", maxHealth=" + this.maxHealth + "}";
/*     */   }
/*     */ }


/* Location:              /home/miguel/Universidad/segundo/ADA/practicas/grupoTarde/PracticaTarde3/towerdefense.jar!/net/agsh/towerdefense/Enemy.class
 * Java compiler version: 20 (64.0)
 * JD-Core Version:       1.1.3
 */