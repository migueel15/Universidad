/*     */ package net.agsh.towerdefense;
/*     */ 
/*     */ import net.agsh.towerdefense.gui.GameWindow;
/*     */ 
/*     */ 
/*     */ public class Game
/*     */ {
/*     */   private static Game instance;
/*   9 */   private Random random = null;
/*  10 */   private long elapsed = 0L; private long remaining = 0L;
/*  11 */   private float timeScaleFactor = 1.0F;
/*     */   private Map map;
/*     */   private Round currentRound;
/*     */   private GameWindow gameWindow;
/*  15 */   private final Config config = new Config();
/*  16 */   private int score = 0;
/*     */   
/*     */   private boolean isPaused = false;
/*     */   
/*     */   public static Game getInstance() {
/*  21 */     if (instance == null) {
/*  22 */       instance = new Game();
/*     */     }
/*  24 */     return instance;
/*     */   }
/*     */   
/*     */   public int getVersion() {
/*  28 */     return 231121;
/*     */   }
/*     */   
/*     */   public Map getMap() {
/*  32 */     return this.map;
/*     */   }
/*     */   
/*     */   public Round getCurrentRound() {
/*  36 */     return this.currentRound;
/*     */   }
/*     */   
/*     */   public void init(long seed) {
/*  40 */     this.random = new Random(seed);
/*  41 */     this
/*     */       
/*  43 */       .map = new Map(new Point2D(this.config.get(Config.Parameter.MAP_SIZE_X), this.config.get(Config.Parameter.MAP_SIZE_Y)), this.config.get(Config.Parameter.MAP_GRID_SPACE));
/*  44 */     this.map.init();
/*     */     
/*  46 */     this.random.pushSeed();
/*  47 */     this.currentRound = new Round();
/*     */     
/*  49 */     if (this.config.get(Config.Parameter.GUI) != 0.0F) {
/*  50 */       this.gameWindow = new GameWindow();
/*     */     }
/*     */   }
/*     */   
/*     */   public void update(long millis) {
/*  55 */     long stepSize = (long)this.config.get(Config.Parameter.GAME_STEP_SIZE);
/*  56 */     long delta = millis + this.remaining;
/*  57 */     while (delta >= stepSize) {
/*  58 */       this.elapsed += stepSize;
/*     */ 
/*     */       
/*  61 */       this.currentRound.update(stepSize);
/*  62 */       if (this.currentRound.ended()) {
/*  63 */         this.score += this.currentRound.getScore();
/*  64 */         delta = 0L;
/*  65 */         System.out.println("Round " + this.currentRound.getRoundNumber() + " score: " + this.currentRound.getScore());
/*     */         
/*  67 */         if (!gameOver()) {
/*  68 */           this
/*     */             
/*  70 */             .map = new Map(new Point2D(this.config.get(Config.Parameter.MAP_SIZE_X), this.config.get(Config.Parameter.MAP_SIZE_Y)), this.config.get(Config.Parameter.MAP_GRID_SPACE));
/*  71 */           this.map.init();
/*     */           
/*  73 */           this.random.popSeed();
/*  74 */           this.random.pushSeed();
/*  75 */           this.currentRound = new Round();
/*     */         } 
/*     */       } 
/*     */       
/*  79 */       delta -= stepSize;
/*     */     } 
/*     */     
/*  82 */     this.remaining = delta;
/*     */   }
/*     */   
/*     */   public void play() {
/*  86 */     this.elapsed = 0L;
/*  87 */     this.remaining = 0L;
/*  88 */     long stepSize = (long)this.config.get(Config.Parameter.GAME_STEP_SIZE);
/*     */     
/*  90 */     if (this.config.get(Config.Parameter.GUI) != 0.0F) {
/*  91 */       long delta = 0L;
/*  92 */       long lastTime = System.currentTimeMillis();
/*  93 */       long sleepTime = 0L;
/*  94 */       while (!gameOver()) {
/*  95 */         delta = System.currentTimeMillis() - lastTime;
/*  96 */         lastTime = System.currentTimeMillis();
/*     */         
/*  98 */         if (!this.isPaused) {
/*  99 */           update((long)((float)delta * this.timeScaleFactor));
/*     */           
/* 101 */           if (delta < stepSize) {
/* 102 */             sleepTime = stepSize - delta;
/*     */             try {
/* 104 */               Thread.sleep(sleepTime);
/* 105 */             } catch (InterruptedException e) {
/* 106 */               e.printStackTrace();
/*     */             } 
/*     */           }  continue;
/*     */         } 
/* 110 */         sleepTime = 100L;
/*     */         try {
/* 112 */           Thread.sleep(sleepTime);
/* 113 */         } catch (InterruptedException e) {
/* 114 */           e.printStackTrace();
/*     */         } 
/*     */       } 
/*     */       
/* 118 */       this.gameWindow.dispose();
/*     */     } else {
/* 120 */       while (!gameOver()) {
/* 121 */         update(stepSize);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean gameOver() {
/* 127 */     return (this.currentRound.ended() && this.currentRound.getRoundNumber() == this.config.get(Config.Parameter.GAME_MAX_ROUNDS) - 1.0F);
/*     */   }
/*     */   
/*     */   public long getElapsedTime() {
/* 131 */     return this.elapsed;
/*     */   }
/*     */   
/*     */   Random getRandom() {
/* 135 */     return this.random;
/*     */   }
/*     */   
/*     */   public int getScore() {
/* 139 */     return this.score;
/*     */   }
/*     */   
/*     */   public float getParam(Config.Parameter parameter) {
/* 143 */     return this.config.get(parameter);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setParam(Config.Parameter parameter, float v) {
/* 148 */     if (this.random == null) {
/* 149 */       this.config.set(parameter, v);
/*     */     } else {
/* 151 */       throw new RuntimeException("Cannot set parameter after the game has been initialized");
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isPaused() {
/* 156 */     return this.isPaused;
/*     */   }
/*     */   
/*     */   public void pause() {
/* 160 */     this.isPaused = true;
/*     */   }
/*     */   
/*     */   public void resume() {
/* 164 */     this.isPaused = false;
/*     */   }
/*     */   
/*     */   public void setTimeScale(float timeScaleFactor) {
/* 168 */     this.timeScaleFactor = timeScaleFactor;
/*     */   }
/*     */ }


/* Location:              /home/miguel/Universidad/segundo/ADA/practicas/grupoTarde/PracticaTarde3/towerdefense.jar!/net/agsh/towerdefense/Game.class
 * Java compiler version: 20 (64.0)
 * JD-Core Version:       1.1.3
 */