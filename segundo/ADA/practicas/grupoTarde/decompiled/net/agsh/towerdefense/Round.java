/*     */ package net.agsh.towerdefense;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import net.agsh.towerdefense.strats.TowerBuyer;
/*     */ import net.agsh.towerdefense.strats.TowerPlacer;
/*     */ 
/*     */ public class Round {
/*     */   private int roundNumber;
/*     */   private ArrayList<Enemy> enemiesWaiting;
/*     */   private ArrayList<Enemy> enemiesAlive;
/*     */   private ArrayList<Enemy> enemiesKilled;
/*     */   private ArrayList<Enemy> enemiesEscaped;
/*  13 */   private long lastSpawn = 0L; private long roundStartTime = Game.getInstance().getElapsedTime();
/*  14 */   float money = 0.0F;
/*  15 */   long maxAllowedTime = 0L;
/*     */   
/*     */   public Round() {
/*  18 */     Game g = Game.getInstance();
/*     */     
/*  20 */     if (g.getCurrentRound() == null) {
/*  21 */       this.roundNumber = 0;
/*     */     } else {
/*  23 */       this.roundNumber = g.getCurrentRound().getRoundNumber() + 1;
/*     */     } 
/*     */     
/*  26 */     System.out.println("Starting round: " + this.roundNumber);
/*     */     
/*  28 */     Point2D size = g.getMap().getSize();
/*  29 */     float separation = g.getParam(Config.Parameter.MAP_GRID_SPACE);
/*     */ 
/*     */     
/*  32 */     int totalEnemies = (int)((this.roundNumber + 1) * g.getParam(Config.Parameter.ENEMY_DENSITY_ROUND_FACTOR) * g.getParam(Config.Parameter.ENEMY_DENSITY) * size.x * size.y / separation * separation);
/*  33 */     System.out.println("Total enemies: " + totalEnemies);
/*  34 */     this.enemiesWaiting = createEnemies(totalEnemies);
/*  35 */     this.enemiesAlive = new ArrayList<>();
/*  36 */     this.enemiesKilled = new ArrayList<>();
/*  37 */     this.enemiesEscaped = new ArrayList<>();
/*     */     
/*  39 */     int numberOfTowers = (int)(g.getParam(Config.Parameter.TOWER_DENSITY) * size.x * size.y / separation * separation);
/*  40 */     System.out.println("Number of available towers: " + numberOfTowers);
/*  41 */     ArrayList<Tower> availableTowers = createTowers(numberOfTowers);
/*  42 */     float totalCost = 0.0F, minCost = Float.MAX_VALUE;
/*  43 */     for (Tower t : availableTowers) {
/*  44 */       if (g.getParam(Config.Parameter.DEBUG_PRINT_TOWER_DETAILS) != 0.0F) System.out.println(t); 
/*  45 */       totalCost += t.getCost();
/*  46 */       if (t.getCost() < minCost) {
/*  47 */         minCost = t.getCost();
/*     */       }
/*     */     } 
/*     */     
/*  51 */     this.money = g.getRandom().nextFloat(totalCost * g.getParam(Config.Parameter.TOWER_MIN_MONEY_FACTOR), totalCost * g
/*  52 */         .getParam(Config.Parameter.TOWER_MAX_MONEY_FACTOR));
/*  53 */     this.money = Math.max(minCost, this.money);
/*  54 */     System.out.println("Money available: " + this.money);
/*     */ 
/*     */     
/*  57 */     ArrayList<Tower> boughtTowers = buyTowers(availableTowers, this.money);
/*  58 */     if (boughtTowers != null) {
/*  59 */       float bougthTotalCost = 0.0F;
/*  60 */       System.out.println("Bought towers: " + boughtTowers.size());
/*  61 */       for (Tower t : boughtTowers) {
/*  62 */         bougthTotalCost += t.getCost();
/*  63 */         if (g.getParam(Config.Parameter.DEBUG_PRINT_TOWER_DETAILS) != 0.0F) System.out.println(t); 
/*     */       } 
/*  65 */       System.out.println("Total cost: " + bougthTotalCost + " (money left: " + this.money - bougthTotalCost + ")");
/*     */ 
/*     */       
/*  68 */       placeTowers(boughtTowers);
/*     */     } else {
/*     */       
/*  71 */       System.out.println("No towers bought");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private ArrayList<Tower> createTowers(int number) {
/*  77 */     ArrayList<Tower> towers = new ArrayList<>();
/*     */     
/*  79 */     int i = 0;
/*  80 */     while (i < number) {
/*  81 */       Tower t = new Tower();
/*     */ 
/*     */       
/*  84 */       boolean isDifferent = true;
/*  85 */       for (Tower t2 : towers) {
/*  86 */         if (t.sameTypeAs(t2)) {
/*  87 */           isDifferent = false;
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/*  92 */       if (isDifferent) {
/*  93 */         towers.add(t);
/*  94 */         i++;
/*     */       } 
/*     */     } 
/*     */     
/*  98 */     return towers;
/*     */   }
/*     */   
/*     */   private ArrayList<Enemy> createEnemies(int number) {
/* 102 */     ArrayList<Enemy> enemies = new ArrayList<>();
/* 103 */     for (int i = 0; i < number; i++) {
/* 104 */       enemies.add(new Enemy());
/*     */     }
/*     */     
/* 107 */     return enemies;
/*     */   }
/*     */   
/*     */   private void placeTowers(ArrayList<Tower> availableTowers) {
/* 111 */     ArrayList<Tower> towersCopy = new ArrayList<>();
/* 112 */     for (Tower t : availableTowers) {
/* 113 */       towersCopy.add(t.clone());
/*     */     }
/*     */     
/* 116 */     Map m = Game.getInstance().getMap();
/* 117 */     for (Tower placedTower : TowerPlacer.placeTowers(towersCopy, m)) {
/* 118 */       for (Tower tower : availableTowers) {
/*     */         
/* 120 */         if (placedTower.sameTypeAs(tower) && 
/* 121 */           !m.addTower(placedTower)) {
/* 122 */           System.out.println("ERROR: tower " + String.valueOf(placedTower.getPosition()) + " placement failed");
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ArrayList<Tower> buyTowers(ArrayList<Tower> availableTowers, float money) {
/* 132 */     ArrayList<Tower> availableTowersCopy = new ArrayList<>();
/* 133 */     for (Tower t : availableTowers) {
/* 134 */       availableTowersCopy.add(t.clone());
/*     */     }
/*     */     
/* 137 */     float spent = 0.0F;
/* 138 */     ArrayList<Integer> selected = TowerBuyer.buyTowers(availableTowersCopy, money);
/*     */ 
/*     */     
/* 141 */     for (int i = 0; i < selected.size(); i++) {
/* 142 */       for (int j = i + 1; j < selected.size(); j++) {
/* 143 */         if (selected.get(i) == selected.get(j)) {
/* 144 */           System.out.println("ERROR: same tower selected twice");
/* 145 */           return null;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 150 */     ArrayList<Tower> boughtTowers = new ArrayList<>();
/* 151 */     for (Iterator<Integer> iterator = selected.iterator(); iterator.hasNext(); ) { int j = ((Integer)iterator.next()).intValue();
/* 152 */       Tower boughtTower = availableTowers.get(j);
/* 153 */       spent += boughtTower.getCost();
/* 154 */       for (Tower t : availableTowers) {
/* 155 */         if (boughtTower.sameTypeAs(t)) {
/* 156 */           boughtTowers.add(boughtTower);
/*     */         }
/*     */       }  }
/*     */ 
/*     */ 
/*     */     
/* 162 */     if (spent > money) {
/* 163 */       System.out.println("ERROR: spent more money than available");
/* 164 */       return null;
/*     */     } 
/*     */     
/* 167 */     return boughtTowers;
/*     */   }
/*     */   
/*     */   public int getRoundNumber() {
/* 171 */     return this.roundNumber;
/*     */   }
/*     */   
/*     */   public void update(long millis) {
/* 175 */     Game g = Game.getInstance();
/*     */     
/* 177 */     updateTowers(millis);
/* 178 */     spawnEnemies(millis);
/* 179 */     updateEnemies(millis);
/*     */   }
/*     */   
/*     */   private void updateTowers(long millis) {
/* 183 */     for (Tower t : Game.getInstance().getMap().getTowers()) {
/* 184 */       t.update(millis);
/*     */     }
/*     */   }
/*     */   
/*     */   private void updateEnemies(long millis) {
/* 189 */     Game g = Game.getInstance();
/* 190 */     ArrayList<Enemy> killedOrEscaped = new ArrayList<>();
/* 191 */     for (Enemy e : this.enemiesAlive) {
/* 192 */       e.update(millis);
/* 193 */       if (e.isDead()) {
/* 194 */         this.enemiesKilled.add(e);
/* 195 */         killedOrEscaped.add(e); continue;
/* 196 */       }  if (e.hasEscaped()) {
/* 197 */         this.enemiesEscaped.add(e);
/* 198 */         killedOrEscaped.add(e);
/*     */       } 
/*     */     } 
/* 201 */     this.enemiesAlive.removeAll(killedOrEscaped);
/*     */   }
/*     */   
/*     */   private void spawnEnemies(long millis) {
/* 205 */     Game g = Game.getInstance();
/* 206 */     int spawnInterval = (int)g.getParam(Config.Parameter.ENEMY_SPAWN_INTERVAL);
/* 207 */     int enemiesToSpawn = (int)((getElapsedTime() - this.lastSpawn) / spawnInterval);
/* 208 */     for (int i = 0; i < Math.min(enemiesToSpawn, this.enemiesWaiting.size()); i++) {
/* 209 */       this.lastSpawn = getElapsedTime();
/* 210 */       Enemy spawnedEnemy = this.enemiesWaiting.get(0);
/* 211 */       this.enemiesWaiting.remove(0);
/* 212 */       MapNode spawnPoint = g.getMap().getStartingPoints().get(g
/* 213 */           .getRandom().nextInt(g.getMap().getStartingPoints().size()));
/* 214 */       spawnedEnemy.setPosition(spawnPoint.getPosition());
/* 215 */       this.enemiesAlive.add(spawnedEnemy);
/*     */     } 
/*     */   }
/*     */   
/*     */   public long getElapsedTime() {
/* 220 */     return Game.getInstance().getElapsedTime() - this.roundStartTime;
/*     */   }
/*     */   
/*     */   public ArrayList<Enemy> getAliveEnemies() {
/* 224 */     return this.enemiesAlive;
/*     */   }
/*     */   
/*     */   public boolean ended() {
/* 228 */     return (this.enemiesWaiting.isEmpty() && this.enemiesAlive.isEmpty());
/*     */   }
/*     */   
/*     */   public int getScore() {
/* 232 */     return this.enemiesEscaped.size();
/*     */   }
/*     */   
/*     */   public ArrayList<Enemy> getEnemiesAlive() {
/* 236 */     return this.enemiesAlive;
/*     */   }
/*     */ }


/* Location:              /home/miguel/Universidad/segundo/ADA/practicas/grupoTarde/PracticaTarde3/towerdefense.jar!/net/agsh/towerdefense/Round.class
 * Java compiler version: 20 (64.0)
 * JD-Core Version:       1.1.3
 */