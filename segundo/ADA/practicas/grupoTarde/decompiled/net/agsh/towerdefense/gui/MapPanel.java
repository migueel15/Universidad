/*     */ package net.agsh.towerdefense.gui;
/*     */ import java.awt.Color;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.image.BufferedImage;
/*     */ import javax.imageio.ImageIO;
/*     */ import net.agsh.towerdefense.Enemy;
/*     */ import net.agsh.towerdefense.Game;
/*     */ import net.agsh.towerdefense.Map;
/*     */ import net.agsh.towerdefense.MapNode;
/*     */ import net.agsh.towerdefense.Obstacle;
/*     */ import net.agsh.towerdefense.Point2D;
/*     */ import net.agsh.towerdefense.Tower;
/*     */ 
/*     */ public class MapPanel extends JPanel {
/*  15 */   float scale = 1.0F;
/*  16 */   float separation = 1.0F;
/*  17 */   int nodeValuesIndex = -1;
/*     */   private Enemy selectedEnemy;
/*     */   private final BufferedImage[][] startTileSet;
/*     */   private final float startScale;
/*     */   private final BufferedImage[][] obstacleTileSet;
/*     */   private final BufferedImage[][] grassTileSet;
/*     */   private int[][] grass;
/*     */   private final BufferedImage[][] pathsTileSet;
/*     */   private final BufferedImage[][] enemyTileSet;
/*     */   private final float enemyScale;
/*     */   private float enemyAnimationSpeed;
/*     */   private final BufferedImage[][] towerTileSet;
/*     */   private final float towerScale;
/*     */   private final int towerAngleOffset;
/*     */   private int[][] paths;
/*  32 */   private int currentRoundNumber = -1;
/*  33 */   private int mapNodesBorder = 0;
/*     */   
/*     */   private boolean showRadius = false;
/*     */   
/*     */   public MapPanel() {
/*     */     try {
/*  39 */       BufferedImage obstacleImage = ImageIO.read(new File("assets/rock.png"));
/*  40 */       this.obstacleTileSet = loadTileSet(obstacleImage, 32);
/*     */       
/*  42 */       BufferedImage grassImage = ImageIO.read(new File("assets/grass.png"));
/*  43 */       this.grassTileSet = loadTileSet(grassImage, 16);
/*     */       
/*  45 */       BufferedImage pathsImage = ImageIO.read(new File("assets/floor_tiles.png"));
/*  46 */       this.pathsTileSet = loadTileSet(pathsImage, 16);
/*     */ 
/*     */ 
/*     */       
/*  50 */       this.startScale = 10.0F;
/*  51 */       this.startTileSet = null;
/*     */       
/*  53 */       BufferedImage enemyImage = ImageIO.read(new File("assets/enemy.png"));
/*  54 */       this.enemyTileSet = loadTileSet(enemyImage, 64);
/*  55 */       this.enemyScale = 4.0F;
/*  56 */       this.enemyAnimationSpeed = 0.01F;
/*     */       
/*  58 */       BufferedImage towerImage = ImageIO.read(new File("assets/turret.png"));
/*  59 */       this.towerTileSet = loadTileSet(towerImage, 74);
/*  60 */       this.towerScale = 1.0F;
/*  61 */       this.towerAngleOffset = 4;
/*     */       
/*  63 */       generateGrass();
/*     */     
/*     */     }
/*  66 */     catch (IOException e) {
/*  67 */       throw new RuntimeException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean get(int x, int y, MapNode[][] nodes) {
/*  72 */     if (x < 0 || x >= (nodes[0]).length || y < 0 || y >= nodes.length) return false; 
/*  73 */     return nodes[y][x].isWalkable();
/*     */   }
/*     */   
/*     */   void generateGrass() {
/*  77 */     if (Game.getInstance().getMap() != null && Game.getInstance().getMap().getNodes() != null) {
/*  78 */       MapNode[][] nodes = Game.getInstance().getMap().getNodes();
/*  79 */       this.grass = new int[nodes.length][(nodes[0]).length];
/*     */       
/*  81 */       Perlin2D perlin = new Perlin2D();
/*  82 */       for (int i = 0; i < this.grass.length; i++) {
/*  83 */         for (int j = 0; j < (this.grass[i]).length; j++) {
/*  84 */           float frequency = 4.0F;
/*  85 */           float depth = 8.0F;
/*  86 */           float roughness = 0.4F;
/*     */           
/*  88 */           float p = (float)perlin.noise((i / frequency), (j / frequency));
/*  89 */           p = (float)(Math.floor((p * depth) + 1.5D) + (Math.random() - 0.5D) * roughness);
/*  90 */           if (p < 0.0F) p = 0.0F; 
/*  91 */           if (p > 2.0F) p = 2.0F; 
/*  92 */           this.grass[i][j] = (int)p;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private BufferedImage[][] loadTileSet(BufferedImage grassImage, int grassTileSize) {
/*  99 */     int rows = grassImage.getHeight() / grassTileSize;
/* 100 */     int cols = grassImage.getWidth() / grassTileSize;
/* 101 */     BufferedImage[][] tileSet = new BufferedImage[rows][cols];
/* 102 */     for (int i = 0; i < rows; i++) {
/* 103 */       for (int j = 0; j < cols; j++) {
/* 104 */         tileSet[i][j] = grassImage.getSubimage(j * grassTileSize, i * grassTileSize, grassTileSize, grassTileSize);
/*     */       }
/*     */     } 
/* 107 */     return tileSet;
/*     */   }
/*     */   
/*     */   public void paintComponent(Graphics g) {
/* 111 */     super.paintComponent(g);
/*     */     
/* 113 */     Game game = Game.getInstance();
/*     */     
/* 115 */     if (game.getCurrentRound().getRoundNumber() != this.currentRoundNumber && game.getMap() != null) {
/* 116 */       this.separation = (game.getMap().getNodeSize()).x;
/* 117 */       generateGrass();
/*     */       
/* 119 */       this.currentRoundNumber = game.getCurrentRound().getRoundNumber();
/*     */     } 
/*     */     
/* 122 */     g.setColor(Color.lightGray);
/* 123 */     g.fillRect(0, 0, getWidth(), getHeight());
/*     */     
/*     */     try {
/* 126 */       game.getParam(Config.Parameter.GAME_STEP_SIZE);
/* 127 */       if (game.getMap() != null) {
/* 128 */         this.scale = getWidth() / (Game.getInstance().getMap().getSize()).x;
/*     */         
/* 130 */         drawTerrain(g, this.scale);
/*     */ 
/*     */         
/* 133 */         g.setColor(Color.black);
/* 134 */         g.drawRect(0, 0, (int)((game.getMap().getSize()).x * this.scale), (int)((game.getMap().getSize()).y * this.scale));
/*     */         
/* 136 */         drawMapNodes(g, this.scale);
/*     */         
/* 138 */         drawStartingPoints(g, this.scale);
/*     */         
/* 140 */         drawEndingPoints(g, this.scale);
/*     */         
/* 142 */         drawObstacles(g, this.scale);
/*     */         
/* 144 */         drawTowers(g, this.scale);
/*     */         
/* 146 */         if (this.nodeValuesIndex >= 0) {
/* 147 */           drawMapNodesValues(g, this.scale, this.nodeValuesIndex);
/*     */         }
/*     */         
/* 150 */         drawEnemies(g, this.scale);
/*     */         
/* 152 */         g.setColor(Color.lightGray);
/* 153 */         g.fillRect(0, (int)((game.getMap().getSize()).y * this.scale) + 1, getWidth(), getHeight());
/*     */       } 
/* 155 */     } catch (ConcurrentModificationException concurrentModificationException) {
/*     */     
/* 157 */     } catch (Exception e) {
/* 158 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Point2D gridPosition(int row, int col) {
/* 169 */     return new Point2D((int)((col * this.separation - this.separation / 2.0F) * this.scale), (int)((row * this.separation - this.separation / 2.0F) * this.scale));
/*     */   }
/*     */   
/*     */   private Point2D gridSize(int row, int col) {
/* 173 */     return new Point2D(((int)(((col + 1) * this.separation - this.separation / 2.0F) * this.scale) - (int)((col * this.separation - this.separation / 2.0F) * this.scale) - this.mapNodesBorder), ((int)(((row + 1) * this.separation - this.separation / 2.0F) * this.scale) - (int)((row * this.separation - this.separation / 2.0F) * this.scale) - this.mapNodesBorder));
/*     */   }
/*     */ 
/*     */   
/*     */   private void drawTerrain(Graphics g, float scale) {
/* 178 */     Game game = Game.getInstance();
/* 179 */     Map map = game.getMap();
/*     */     
/* 181 */     if (Game.getInstance().getMap().getWalkableNodes() != null) {
/* 182 */       for (MapNode node : Game.getInstance().getMap().getWalkableNodes()) {
/* 183 */         g.setColor(Color.orange);
/* 184 */         int size = 3;
/* 185 */         g.fillRect((int)((node.getPosition()).x * scale), (int)((node.getPosition()).y * scale), size, size);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 190 */     if (map != null && map.getNodes() != null) {
/* 191 */       MapNode[][] nodes = Game.getInstance().getMap().getNodes();
/* 192 */       float maxEnemyRadius = game.getParam(Config.Parameter.ENEMY_RADIUS_MAX);
/* 193 */       for (int i = 0; i < nodes.length; i++) {
/* 194 */         for (int j = 0; j < (nodes[i]).length; j++) {
/* 195 */           if (nodes[i][j].isWalkable()) {
/*     */             
/* 197 */             boolean pathTop = get(j, i - 1, nodes);
/* 198 */             boolean pathLeft = get(j - 1, i, nodes);
/* 199 */             boolean pathRight = get(j + 1, i, nodes);
/* 200 */             boolean pathBottom = get(j, i + 1, nodes);
/*     */ 
/*     */             
/* 203 */             int topLeftTile = pathTop ? (pathLeft ? 4 : 2) : (pathLeft ? 1 : 0);
/* 204 */             int topRightTile = pathTop ? (pathRight ? 4 : 8) : (pathRight ? 1 : 6);
/* 205 */             int bottomLeftTile = pathBottom ? (pathLeft ? 4 : 2) : (pathLeft ? 13 : 12);
/* 206 */             int bottomRightTile = pathBottom ? (pathRight ? 4 : 8) : (pathRight ? 13 : 18);
/*     */             
/* 208 */             BufferedImage tile1 = this.pathsTileSet[topLeftTile / (this.pathsTileSet[0]).length][topLeftTile % (this.pathsTileSet[0]).length];
/* 209 */             BufferedImage tile2 = this.pathsTileSet[topRightTile / (this.pathsTileSet[0]).length][topRightTile % (this.pathsTileSet[0]).length];
/* 210 */             BufferedImage tile3 = this.pathsTileSet[bottomLeftTile / (this.pathsTileSet[0]).length][bottomLeftTile % (this.pathsTileSet[0]).length];
/* 211 */             BufferedImage tile4 = this.pathsTileSet[bottomRightTile / (this.pathsTileSet[0]).length][bottomRightTile % (this.pathsTileSet[0]).length];
/*     */             
/* 213 */             Point2D tileTopLeft = gridPosition(i, j);
/* 214 */             Point2D tileSize = gridSize(i, j);
/* 215 */             int xHalfSize = (int)(tileSize.x / 2.0F);
/* 216 */             int yHalfSize = (int)(tileSize.y / 2.0F);
/* 217 */             g.drawImage(tile1, (int)tileTopLeft.x, (int)tileTopLeft.y, xHalfSize, yHalfSize, null);
/* 218 */             g.drawImage(tile2, (int)tileTopLeft.x + xHalfSize, (int)tileTopLeft.y, (int)(tileSize.x - xHalfSize), yHalfSize, null);
/* 219 */             g.drawImage(tile3, (int)tileTopLeft.x, (int)tileTopLeft.y + yHalfSize, xHalfSize, (int)(tileSize.y - yHalfSize), null);
/* 220 */             g.drawImage(tile4, (int)tileTopLeft.x + xHalfSize, (int)tileTopLeft.y + yHalfSize, (int)(tileSize.x - xHalfSize), (int)(tileSize.y - yHalfSize), null);
/*     */             
/* 222 */             if (this.showRadius) {
/* 223 */               g.setColor(new Color(0.0F, 0.5F, 0.7F, 0.3F));
/* 224 */               g.fillOval((int)(((nodes[i][j].getPosition()).x - maxEnemyRadius) * scale), 
/* 225 */                   (int)(((nodes[i][j].getPosition()).y - maxEnemyRadius) * scale), (int)(2.0F * maxEnemyRadius * scale), (int)(2.0F * maxEnemyRadius * scale));
/*     */             }
/*     */           
/*     */           } else {
/*     */             
/* 230 */             BufferedImage tile = this.grassTileSet[0][this.grass[i][j]];
/* 231 */             Point2D tileTopLeft = gridPosition(i, j);
/* 232 */             Point2D tileSize = gridSize(i, j);
/* 233 */             int x = (int)tileTopLeft.x;
/* 234 */             int y = (int)tileTopLeft.y;
/* 235 */             int w = (int)tileSize.x - this.mapNodesBorder;
/* 236 */             int h = (int)tileSize.y - this.mapNodesBorder;
/* 237 */             g.drawImage(tile, x, y, w, h, null);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void drawMapNodes(Graphics g, float scale) {
/* 245 */     Random random = new Random(0L);
/* 246 */     if (Game.getInstance().getMap().getNodes() != null) {
/* 247 */       for (MapNode[] nodes : Game.getInstance().getMap().getNodes()) {
/* 248 */         for (MapNode node : nodes) {
/* 249 */           g.setColor(Color.gray);
/* 250 */           int size = 1;
/* 251 */           g.fillRect((int)((node.getPosition()).x * scale), (int)((node.getPosition()).y * scale), size, size);
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private Color gradient(Color maxColor, Color minColor, float percentage, float alpha) {
/* 259 */     float r = maxColor.getRed() * percentage + minColor.getRed() * (1.0F - percentage);
/* 260 */     float g = maxColor.getGreen() * percentage + minColor.getGreen() * (1.0F - percentage);
/* 261 */     float b = maxColor.getBlue() * percentage + minColor.getBlue() * (1.0F - percentage);
/* 262 */     return new Color(r / 255.0F, g / 255.0F, b / 255.0F, alpha);
/*     */   }
/*     */   
/*     */   private void drawMapNodesValues(Graphics g, float scale, int index) {
/* 266 */     Map m = Game.getInstance().getMap();
/* 267 */     if (m.getNodes() != null) {
/* 268 */       float mapNodeSize = (m.getNodeSize()).x;
/*     */       
/* 270 */       float minValue = Float.MAX_VALUE, maxValue = 0.0F;
/* 271 */       for (MapNode[] nodes : m.getNodes()) {
/* 272 */         for (MapNode node : nodes) {
/* 273 */           if (node.getValue(index) >= 0.0F) {
/*     */             
/* 275 */             if (node.getValue(index) > maxValue) {
/* 276 */               maxValue = node.getValue(index);
/*     */             }
/* 278 */             if (node.getValue(index) < minValue) {
/* 279 */               minValue = node.getValue(index);
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/*     */       
/* 285 */       if (minValue != maxValue) {
/* 286 */         float maxValueInv = 1.0F / (maxValue - minValue);
/*     */         
/* 288 */         int size = Math.max((int)(mapNodeSize * scale), 1);
/* 289 */         int halfSize = size / 2;
/* 290 */         for (MapNode[] nodes : m.getNodes()) {
/* 291 */           for (MapNode node : nodes) {
/* 292 */             if (node.getValue(index) >= 0.0F) {
/* 293 */               float val = Math.min(1.0F, Math.max(0.0F, (node.getValue(index) - minValue) * maxValueInv));
/* 294 */               g.setColor(gradient(Color.green, Color.red, val, 0.5F));
/* 295 */               g.fillRect((int)(((node.getPosition()).x - halfSize) * scale), 
/* 296 */                   (int)(((node.getPosition()).y - halfSize) * scale), size, size);
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void drawEndingPoints(Graphics g, float scale) {
/* 306 */     if (Game.getInstance().getMap().getEndingPoints() != null) {
/* 307 */       for (MapNode p : Game.getInstance().getMap().getEndingPoints()) {
/* 308 */         g.setColor(Color.red);
/* 309 */         float radius = 5.0F;
/* 310 */         g.fillRect((int)(((p.getPosition()).x - radius) * scale), (int)(((p.getPosition()).y - radius) * scale), (int)(2.0F * radius * scale), (int)(2.0F * radius * scale));
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private void drawStartingPoints(Graphics g, float scale) {
/* 317 */     if (Game.getInstance().getMap().getStartingPoints() != null) {
/* 318 */       for (MapNode p : Game.getInstance().getMap().getStartingPoints()) {
/* 319 */         float radius = 5.0F;
/* 320 */         if (this.startTileSet != null) {
/* 321 */           BufferedImage tile = this.startTileSet[0][0];
/* 322 */           g.drawImage(tile, (int)(((p.getPosition()).x - radius / 2.0F * this.startScale) * scale), 
/* 323 */               (int)(((p.getPosition()).y - radius / 2.0F * this.startScale) * scale), (int)(radius * scale * this.startScale), (int)(radius * scale * this.startScale), null);
/*     */           continue;
/*     */         } 
/* 326 */         g.setColor(Color.green);
/* 327 */         g.fillRect((int)(((p.getPosition()).x - radius) * scale), (int)(((p.getPosition()).y - radius) * scale), (int)(2.0F * radius * scale), (int)(2.0F * radius * scale));
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void drawObstacles(Graphics g, float scale) {
/* 335 */     ArrayList<Obstacle> obstacles = Game.getInstance().getMap().getObstacles();
/* 336 */     if (obstacles != null) {
/* 337 */       for (Obstacle o : obstacles) {
/* 338 */         g.setColor(Color.black);
/* 339 */         int size = Math.max((int)(2.0F * o.getRadius() * scale), 1);
/* 340 */         if (this.obstacleTileSet != null) {
/* 341 */           BufferedImage tile = this.obstacleTileSet[0][0];
/* 342 */           g.drawImage(tile, (int)(((o.getPosition()).x - o.getRadius()) * scale), 
/* 343 */               (int)(((o.getPosition()).y - o.getRadius()) * scale), size, size, null);
/*     */ 
/*     */           
/* 346 */           if (this.showRadius) {
/* 347 */             g.setColor(new Color(0.0F, 0.0F, 0.0F, 0.5F));
/* 348 */             g.fillOval((int)(((o.getPosition()).x - o.getRadius()) * scale), 
/* 349 */                 (int)(((o.getPosition()).y - o.getRadius()) * scale), size, size);
/*     */           } 
/*     */           continue;
/*     */         } 
/* 353 */         g.fillOval((int)(((o.getPosition()).x - o.getRadius()) * scale), 
/* 354 */             (int)(((o.getPosition()).y - o.getRadius()) * scale), size, size);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void drawTowers(Graphics g, float scale) {
/* 362 */     if (Game.getInstance().getMap().getTowers() != null) {
/* 363 */       for (Tower t : Game.getInstance().getMap().getTowers()) {
/* 364 */         int size = Math.max((int)(2.0F * t.getRange() * scale), 1);
/* 365 */         if (this.showRadius) {
/* 366 */           g.setColor(Color.orange);
/* 367 */           g.drawOval((int)(((t.getPosition()).x - t.getRange()) * scale), 
/* 368 */               (int)(((t.getPosition()).y - t.getRange()) * scale), size, size);
/*     */         } 
/*     */ 
/*     */         
/* 372 */         size = Math.max((int)(2.0F * t.getRadius() * scale), 1);
/* 373 */         if (this.towerTileSet != null) {
/* 374 */           float angle = 3.1415927F;
/* 375 */           if (t.getTarget() != null) {
/* 376 */             angle = (float)Math.atan2(((t.getTarget()).y - (t.getPosition()).y), ((t.getTarget()).x - (t.getPosition()).x));
/*     */           }
/* 378 */           if (angle < 0.0F) angle = (float)(angle + 6.283185307179586D);
/*     */           
/* 380 */           int tileIndex = (int)Math.round(angle / 6.283185307179586D * (this.towerTileSet[0]).length);
/* 381 */           tileIndex = (tileIndex + this.towerAngleOffset) % (this.towerTileSet[0]).length;
/* 382 */           BufferedImage tile = this.towerTileSet[0][tileIndex];
/* 383 */           g.drawImage(tile, (int)(((t.getPosition()).x - t.getRadius() * this.towerScale) * scale), 
/* 384 */               (int)(((t.getPosition()).y - t.getRadius() * this.towerScale) * scale), (int)(size * this.towerScale), (int)(size * this.towerScale), null);
/*     */ 
/*     */           
/* 387 */           if (this.showRadius) {
/* 388 */             g.setColor(new Color(1.0F, 0.5F, 0.5F, 0.5F));
/* 389 */             g.fillOval((int)(((t.getPosition()).x - t.getRadius()) * scale), 
/* 390 */                 (int)(((t.getPosition()).y - t.getRadius()) * scale), size, size);
/*     */           } 
/*     */         } else {
/*     */           
/* 394 */           g.fillOval((int)(((t.getPosition()).x - t.getRadius()) * scale), 
/* 395 */               (int)(((t.getPosition()).y - t.getRadius()) * scale), size, size);
/*     */         } 
/*     */ 
/*     */         
/* 399 */         int cooldownBarWidth = size;
/* 400 */         float cooldownPercentage = t.getCooldownLeft() / t.getCooldown();
/* 401 */         g.setColor(Color.blue);
/* 402 */         g.fillRect((int)(((t.getPosition()).x - (size / 2)) * scale), 
/* 403 */             (int)(((t.getPosition()).y - (size / 2)) * scale), (int)(cooldownBarWidth * cooldownPercentage), 2);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private void drawEnemies(Graphics g, float scale) {
/* 410 */     Game game = Game.getInstance();
/* 411 */     Round r = game.getCurrentRound();
/* 412 */     if (r.getAliveEnemies() != null) {
/* 413 */       for (Enemy enemy : r.getAliveEnemies()) {
/*     */ 
/*     */         
/* 416 */         if (this.selectedEnemy != null && enemy == this.selectedEnemy && enemy.getPath() != null && enemy.getPath().size() > 0) {
/* 417 */           g.setColor(Color.white);
/* 418 */           g.drawLine((int)((enemy.getPosition()).x * scale), 
/* 419 */               (int)((enemy.getPosition()).y * scale), 
/* 420 */               (int)((((MapNode)enemy.getPath().get(0)).getPosition()).x * scale), 
/* 421 */               (int)((((MapNode)enemy.getPath().get(0)).getPosition()).y * scale));
/* 422 */           for (int i = 0; i < enemy.getPath().size() - 1; i++) {
/* 423 */             g.drawLine((int)((((MapNode)enemy.getPath().get(i)).getPosition()).x * scale), 
/* 424 */                 (int)((((MapNode)enemy.getPath().get(i)).getPosition()).y * scale), 
/* 425 */                 (int)((((MapNode)enemy.getPath().get(i + 1)).getPosition()).x * scale), 
/* 426 */                 (int)((((MapNode)enemy.getPath().get(i + 1)).getPosition()).y * scale));
/*     */           }
/*     */         } 
/*     */ 
/*     */         
/* 431 */         float radius = enemy.getRadius();
/* 432 */         int size = Math.max((int)(2.0F * radius * scale), 1);
/* 433 */         if (this.enemyTileSet != null) {
/* 434 */           int enemyType = enemy.getId() % this.enemyTileSet.length;
/* 435 */           BufferedImage tile = this.enemyTileSet[enemyType][(int)((enemy.getId() + (float)r.getElapsedTime() * this.enemyAnimationSpeed) % (this.enemyTileSet[0]).length)];
/* 436 */           g.drawImage(tile, (int)(((enemy.getPosition()).x - radius * this.enemyScale) * scale), 
/* 437 */               (int)(((enemy.getPosition()).y - radius * this.enemyScale) * scale), (int)(size * this.enemyScale), (int)(size * this.enemyScale), null);
/*     */ 
/*     */           
/* 440 */           if (this.showRadius) {
/* 441 */             g.setColor(new Color(0.0F, 0.0F, 1.0F, 0.5F));
/* 442 */             g.fillOval((int)(((enemy.getPosition()).x - radius) * scale), 
/* 443 */                 (int)(((enemy.getPosition()).y - radius) * scale), size, size);
/*     */           } 
/*     */         } else {
/*     */           
/* 447 */           g.setColor(Color.blue);
/* 448 */           g.fillOval((int)(((enemy.getPosition()).x - radius) * scale), 
/* 449 */               (int)(((enemy.getPosition()).y - radius) * scale), size, size);
/*     */         } 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 455 */         int healthBarWidth = size;
/* 456 */         float healthPercentage = enemy.getHealth() / enemy.getMaxHealth();
/* 457 */         g.setColor(Color.red);
/* 458 */         g.fillRect((int)(((enemy.getPosition()).x - radius) * scale), 
/* 459 */             (int)(((enemy.getPosition()).y - radius - 10.0F) * scale), (int)(healthBarWidth * healthPercentage), 2);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public float getScale() {
/* 466 */     return this.scale;
/*     */   }
/*     */   
/*     */   public void setNodeValuesIndex(int i) {
/* 470 */     this.nodeValuesIndex = i;
/*     */   }
/*     */   
/*     */   public void setSelectedEnemy(Enemy enemy) {
/* 474 */     this.selectedEnemy = enemy;
/*     */   }
/*     */   
/*     */   public void setShowGrid(boolean showGrid) {
/* 478 */     if (showGrid) {
/* 479 */       this.mapNodesBorder = 1;
/*     */     } else {
/* 481 */       this.mapNodesBorder = 0;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setShowRadius(boolean showRadius) {
/* 486 */     this.showRadius = showRadius;
/*     */   }
/*     */ }


/* Location:              /home/miguel/Universidad/segundo/ADA/practicas/grupoTarde/PracticaTarde3/towerdefense.jar!/net/agsh/towerdefense/gui/MapPanel.class
 * Java compiler version: 20 (64.0)
 * JD-Core Version:       1.1.3
 */