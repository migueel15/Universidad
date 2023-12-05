/*     */ package net.agsh.towerdefense;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ 
/*     */ public class Map {
/*     */   ArrayList<MapNode> startingPoints;
/*     */   ArrayList<MapNode> endingPoints;
/*     */   Point2D size;
/*     */   float separation;
/*     */   ArrayList<Obstacle> obstacles;
/*     */   private ArrayList<Tower> towers;
/*     */   MapNode[][] nodes;
/*     */   ArrayList<MapNode> walkableNodes;
/*     */   
/*     */   public Map(Point2D size, float separation) {
/*  16 */     this.size = size;
/*  17 */     this.separation = separation;
/*     */   }
/*     */ 
/*     */   
/*     */   public void init() {
/*  22 */     tessellate();
/*     */     
/*  24 */     createStartingPoints();
/*     */     
/*  26 */     createEndingPoints();
/*     */     
/*  28 */     selectWalkableNodes();
/*     */     
/*  30 */     createObstacles();
/*     */     
/*  32 */     this.towers = new ArrayList<>();
/*     */   }
/*     */   
/*     */   private void createEndingPoints() {
/*  36 */     Game g = Game.getInstance();
/*  37 */     Random r = Game.getInstance().getRandom();
/*     */     
/*  39 */     int pointsCount = (int)g.getParam(Config.Parameter.MAP_ENDING_POINTS);
/*  40 */     float pointsSeparationFactor = g.getParam(Config.Parameter.MAP_ENDING_POINTS_SEPARATION_FACTOR);
/*  41 */     float maxDistanceAmongPoints = this.size.y / pointsCount;
/*     */     
/*  43 */     this.endingPoints = new ArrayList<>();
/*  44 */     while (this.endingPoints.size() < pointsCount) {
/*  45 */       MapNode n = nearestMapNode(new Point2D(this.size.x, r.nextFloat(this.size.y)));
/*     */       
/*  47 */       float distanceToClosestStartingPoint = Float.MAX_VALUE;
/*  48 */       for (MapNode endingPoint : this.endingPoints) {
/*  49 */         float distance = endingPoint.getPosition().distance(n.getPosition());
/*  50 */         if (distance < distanceToClosestStartingPoint) {
/*  51 */           distanceToClosestStartingPoint = distance;
/*     */         }
/*     */       } 
/*     */       
/*  55 */       if (!this.endingPoints.contains(n) && distanceToClosestStartingPoint > pointsSeparationFactor * maxDistanceAmongPoints) {
/*  56 */         this.endingPoints.add(n);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private void createStartingPoints() {
/*  62 */     Game g = Game.getInstance();
/*  63 */     Random r = Game.getInstance().getRandom();
/*     */     
/*  65 */     int pointsCount = (int)g.getParam(Config.Parameter.MAP_STARTING_POINTS);
/*  66 */     float pointsSeparationFactor = g.getParam(Config.Parameter.MAP_STARTING_POINTS_SEPARATION_FACTOR);
/*  67 */     float maxDistanceAmongPoints = this.size.y / pointsCount;
/*     */     
/*  69 */     this.startingPoints = new ArrayList<>();
/*  70 */     while (this.startingPoints.size() < pointsCount) {
/*  71 */       MapNode n = nearestMapNode(new Point2D(0.0F, r.nextFloat(this.separation, this.size.y - this.separation)));
/*     */       
/*  73 */       float distanceToClosestStartingPoint = Float.MAX_VALUE;
/*  74 */       for (MapNode startingPoint : this.startingPoints) {
/*  75 */         float distance = startingPoint.getPosition().distance(n.getPosition());
/*  76 */         if (distance < distanceToClosestStartingPoint) {
/*  77 */           distanceToClosestStartingPoint = distance;
/*     */         }
/*     */       } 
/*     */       
/*  81 */       if (!this.startingPoints.contains(n) && distanceToClosestStartingPoint > pointsSeparationFactor * maxDistanceAmongPoints) {
/*  82 */         this.startingPoints.add(n);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void selectWalkableNodes() {
/*  89 */     Game g = Game.getInstance();
/*  90 */     Random r = g.getRandom();
/*     */     
/*  92 */     this.walkableNodes = new ArrayList<>();
/*     */     
/*  94 */     for (MapNode start : this.startingPoints) {
/*  95 */       for (MapNode end : this.endingPoints) {
/*  96 */         ArrayList<MapNode> openNodes = getNodesList();
/*  97 */         openNodes.removeIf(n -> ((n.getPosition()).x == 0.0F || (n.getPosition()).x == this.size.x || (n.getPosition()).y == 0.0F || (n.getPosition()).y == this.size.y));
/*     */ 
/*     */         
/* 100 */         ArrayList<MapNode> forcedNodes = new ArrayList<>();
/* 101 */         forcedNodes.add(start);
/* 102 */         forcedNodes.add(end);
/*     */         
/* 104 */         ArrayList<MapNode> validNodes = new ArrayList<>(openNodes);
/* 105 */         validNodes.add(start);
/* 106 */         validNodes.add(end);
/* 107 */         ArrayList<MapNode> witness = findPath(start, end, validNodes);
/* 108 */         while (witness != null) {
/* 109 */           if (openNodes.isEmpty()) {
/* 110 */             for (MapNode n : witness) {
/* 111 */               int i = getGridRow(n);
/* 112 */               int j = getGridColumn(n);
/* 113 */               if (!this.nodes[i][j].isWalkable()) {
/* 114 */                 this.nodes[i][j].setWalkable(true);
/* 115 */                 this.walkableNodes.add(n);
/*     */               } 
/*     */             } 
/*     */             
/*     */             break;
/*     */           } 
/* 121 */           MapNode c = openNodes.get(r.nextInt(openNodes.size()));
/* 122 */           openNodes.remove(c);
/* 123 */           if (witness.contains(c)) {
/* 124 */             ArrayList<MapNode> validNodes2 = new ArrayList<>(openNodes);
/* 125 */             validNodes2.addAll(forcedNodes);
/* 126 */             ArrayList<MapNode> newPath = findPath(start, end, validNodes2);
/* 127 */             if (newPath == null) {
/* 128 */               forcedNodes.add(c); continue;
/*     */             } 
/* 130 */             witness = newPath;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     while (true) {
/* 140 */       MapNode node = null;
/* 141 */       for (MapNode n : this.walkableNodes) {
/* 142 */         int walkableNeighbors = 0;
/* 143 */         for (MapNode neighbor : n.getNeighbors()) {
/* 144 */           if (this.walkableNodes.contains(neighbor)) {
/* 145 */             walkableNeighbors++;
/*     */           }
/*     */         } 
/* 148 */         if (walkableNeighbors > 4) {
/* 149 */           node = n;
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/* 154 */       if (node == null) {
/*     */         break;
/*     */       }
/* 157 */       this.walkableNodes.remove(node);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getGridRow(MapNode n) {
/* 170 */     return Math.round((n.getPosition()).y / this.separation);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getGridColumn(MapNode n) {
/* 179 */     return Math.round((n.getPosition()).x / this.separation);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ArrayList<MapNode> getNodesList() {
/* 187 */     ArrayList<MapNode> nodesList = new ArrayList<>();
/* 188 */     for (int i = 0; i < this.nodes.length; i++) {
/* 189 */       for (int j = 0; j < (this.nodes[i]).length; j++) {
/* 190 */         nodesList.add(this.nodes[i][j]);
/*     */       }
/*     */     } 
/* 193 */     return nodesList;
/*     */   }
/*     */   
/*     */   private float pathTotalDistance(ArrayList<MapNode> path) {
/* 197 */     float distance = 0.0F;
/* 198 */     for (int i = 0; i < path.size() - 1; i++) {
/* 199 */       distance += ((MapNode)path.get(i)).getPosition().distance(((MapNode)path.get(i + 1)).getPosition());
/*     */     }
/* 201 */     return distance;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MapNode nearestMapNode(Point2D point2D) {
/* 210 */     int i = Math.round(point2D.y / this.separation);
/* 211 */     i = Math.max(0, Math.min(this.nodes.length - 1, i));
/* 212 */     int j = Math.round(point2D.x / this.separation);
/* 213 */     j = Math.max(0, Math.min((this.nodes[i]).length - 1, j));
/*     */     
/* 215 */     return this.nodes[i][j];
/*     */   }
/*     */ 
/*     */   
/*     */   private void tessellate() {
/* 220 */     this.nodes = new MapNode[(int)(this.size.y / this.separation) + 1][(int)(this.size.x / this.separation) + 1];
/* 221 */     int i = 0, j = 0;
/* 222 */     for (float y = 0.0F; y <= this.size.y; y += this.separation, i++) {
/* 223 */       j = 0;
/* 224 */       for (float x = 0.0F; x <= this.size.x; x += this.separation, j++) {
/* 225 */         this.nodes[i][j] = new MapNode(new Point2D(x, y));
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 230 */     for (i = 0; i < this.nodes.length; i++) {
/* 231 */       for (j = 0; j < (this.nodes[i]).length; j++) {
/* 232 */         if (i > 0) {
/* 233 */           this.nodes[i][j].addNeighbor(this.nodes[i - 1][j]);
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 241 */         if (i < this.nodes.length - 1) {
/* 242 */           this.nodes[i][j].addNeighbor(this.nodes[i + 1][j]);
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 250 */         if (j > 0) {
/* 251 */           this.nodes[i][j].addNeighbor(this.nodes[i][j - 1]);
/*     */         }
/* 253 */         if (j < (this.nodes[i]).length - 1) {
/* 254 */           this.nodes[i][j].addNeighbor(this.nodes[i][j + 1]);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void createObstacles() {
/* 261 */     Game g = Game.getInstance();
/* 262 */     Random r = Game.getInstance().getRandom();
/*     */     
/* 264 */     int count = (int)(g.getParam(Config.Parameter.OBSTACLE_DENSITY) * this.size.x * this.size.y / this.separation * this.separation);
/*     */     
/* 266 */     this.obstacles = new ArrayList<>();
/* 267 */     int maxTries = (int)g.getParam(Config.Parameter.OBSTACLE_MAX_TRIES);
/* 268 */     int i = 0;
/* 269 */     while (i < count && maxTries > 0) {
/* 270 */       Obstacle o = new Obstacle(r.nextFloat(g.getParam(Config.Parameter.OBSTACLE_RADIUS_MIN), g
/* 271 */             .getParam(Config.Parameter.OBSTACLE_RADIUS_MAX)));
/*     */       
/* 273 */       Point2D position = this.nodes[r.nextInt(this.nodes.length)][r.nextInt((this.nodes[0]).length)].getPosition();
/*     */       
/* 275 */       if (!collidesWithObstacles(position, o.getRadius(), this.obstacles) && 
/* 276 */         !collidesWithWalkableNodes(position, o
/* 277 */           .getRadius() + g.getParam(Config.Parameter.ENEMY_RADIUS_MAX), this.walkableNodes)) {
/* 278 */         o.setPosition(position);
/* 279 */         this.obstacles.add(o);
/* 280 */         i++;
/* 281 */         maxTries = (int)g.getParam(Config.Parameter.OBSTACLE_MAX_TRIES); continue;
/*     */       } 
/* 283 */       maxTries--;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   ArrayList<MapNode> findPath(MapNode origin, MapNode destination, ArrayList<MapNode> validNodes) {
/* 290 */     boolean debug = false;
/* 291 */     if (debug) System.out.println("Finding path from " + String.valueOf(origin.getPosition()) + " to " + String.valueOf(destination.getPosition())); 
/* 292 */     Game g = Game.getInstance();
/* 293 */     float maxEnemyRadius = g.getParam(Config.Parameter.ENEMY_RADIUS_MAX);
/*     */     
/* 295 */     ArrayList<AStarNode> open = new ArrayList<>();
/* 296 */     ArrayList<AStarNode> closed = new ArrayList<>();
/*     */     
/* 298 */     AStarNode current = new AStarNode(origin);
/* 299 */     current.setG(0.0F);
/* 300 */     current.setH(current.mapNode.getPosition().manhattanDistance(destination.getPosition()));
/* 301 */     open.add(current);
/* 302 */     if (debug) System.out.println("Adding " + String.valueOf(current.getMapNode().getPosition()) + " to open list");
/*     */     
/* 304 */     while (!open.isEmpty()) {
/* 305 */       current = open.get(0);
/* 306 */       for (AStarNode n : open) {
/* 307 */         if (n.getF() < current.getF()) {
/* 308 */           current = n;
/*     */         }
/*     */       } 
/* 311 */       if (debug) System.out.println("Current node is " + String.valueOf(current.getMapNode().getPosition()) + " (moved to closed list)");
/*     */       
/* 313 */       open.remove(current);
/* 314 */       closed.add(current);
/*     */       
/* 316 */       if (current.getMapNode() == destination) {
/* 317 */         if (debug) System.out.println("Found path"); 
/* 318 */         ArrayList<MapNode> path = new ArrayList<>();
/* 319 */         while (current != null) {
/* 320 */           path.add(0, current.getMapNode());
/* 321 */           current = current.getParent();
/*     */         } 
/* 323 */         return path;
/*     */       } 
/*     */       
/* 326 */       for (MapNode n : current.getMapNode().getNeighbors()) {
/* 327 */         if (debug) System.out.print(" Neighbor: " + String.valueOf(n.getPosition())); 
/* 328 */         if (validNodes.contains(n) && (this.obstacles == null || 
/* 329 */           !collidesWithObstacles(n.getPosition(), maxEnemyRadius, this.obstacles)) && (this.towers == null || 
/* 330 */           !collidesWithTowers(n.getPosition(), maxEnemyRadius, this.towers))) {
/*     */           
/* 332 */           AStarNode neighbor = new AStarNode(n);
/* 333 */           if (debug) System.out.print(" F=" + neighbor.getF()); 
/* 334 */           if (!closed.contains(neighbor)) {
/* 335 */             if (!open.contains(neighbor)) {
/*     */ 
/*     */               
/* 338 */               Point2D midPoint = current.getMapNode().getPosition().midPoint(n.getPosition());
/* 339 */               if ((this.obstacles == null || !collidesWithObstacles(midPoint, maxEnemyRadius, this.obstacles)) && (this.towers == null || 
/* 340 */                 !collidesWithTowers(midPoint, maxEnemyRadius, this.towers))) {
/*     */                 
/* 342 */                 neighbor.setG(current.getG() + current.getMapNode().getPosition().distance(n.getPosition()));
/* 343 */                 neighbor.setH(n.getPosition().manhattanDistance(destination.getPosition()));
/* 344 */                 neighbor.setParent(current);
/*     */                 
/* 346 */                 open.add(neighbor);
/*     */                 
/* 348 */                 if (debug) System.out.println(" added to open list");  continue;
/*     */               } 
/* 350 */               if (debug) System.out.println(" non traversable mid point"); 
/*     */               continue;
/*     */             } 
/* 353 */             if (debug) System.out.println(" already in open list"); 
/*     */             continue;
/*     */           } 
/* 356 */           neighbor.setG(current.getG() + current.getMapNode().getPosition().distance(n.getPosition()));
/* 357 */           neighbor.setH(n.getPosition().manhattanDistance(destination.getPosition()));
/* 358 */           neighbor.setParent(current);
/*     */ 
/*     */           
/* 361 */           AStarNode closedNeighbor = closed.get(closed.indexOf(neighbor));
/* 362 */           if (neighbor.getG() < closedNeighbor.getG()) {
/* 363 */             closed.remove(closedNeighbor);
/* 364 */             neighbor.setParent(current);
/* 365 */             open.add(neighbor);
/*     */             
/* 367 */             if (debug) System.out.println(" updated and added to open list");  continue;
/*     */           } 
/* 369 */           if (debug) System.out.println(" already in closed list");
/*     */           
/*     */           continue;
/*     */         } 
/* 373 */         if (debug) System.out.println(" collides with obstacle or tower");
/*     */       
/*     */       } 
/*     */     } 
/*     */     
/* 378 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ArrayList<MapNode> getStartingPoints() {
/* 386 */     return this.startingPoints;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ArrayList<MapNode> getEndingPoints() {
/* 394 */     return this.endingPoints;
/*     */   }
/*     */   
/*     */   boolean isInsideMap(Point2D position, float radius) {
/* 398 */     if (position.x < radius || position.x > this.size.x - radius || position.y < radius || position.y > this.size.y - radius) {
/* 399 */       return false;
/*     */     }
/* 401 */     return true;
/*     */   }
/*     */   
/*     */   boolean collidesWithObstacles(Point2D position, float radius, ArrayList<Obstacle> obstacles) {
/* 405 */     for (Obstacle o : obstacles) {
/* 406 */       if (position.distance(o.getPosition()) < radius + o.getRadius()) {
/* 407 */         return true;
/*     */       }
/*     */     } 
/* 410 */     return false;
/*     */   }
/*     */   
/*     */   boolean collidesWithTowers(Point2D position, float radius, ArrayList<Tower> towers) {
/* 414 */     for (Tower t : towers) {
/* 415 */       if (position.distance(t.getPosition()) < radius + t.getRadius()) {
/* 416 */         return true;
/*     */       }
/*     */     } 
/* 419 */     return false;
/*     */   }
/*     */   
/*     */   boolean collidesWithWalkableNodes(Point2D position, float radius, ArrayList<MapNode> walkableNodes) {
/* 423 */     for (MapNode n : walkableNodes) {
/* 424 */       if (position.distance(n.getPosition()) < radius) {
/* 425 */         return true;
/*     */       }
/*     */     } 
/* 428 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MapNode[][] getNodes() {
/* 436 */     return this.nodes;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ArrayList<Obstacle> getObstacles() {
/* 444 */     return this.obstacles;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Point2D getSize() {
/* 452 */     return this.size;
/*     */   }
/*     */   
/*     */   boolean addTower(Tower tower) {
/* 456 */     Game g = Game.getInstance();
/* 457 */     float maxEnemyRadius = g.getParam(Config.Parameter.ENEMY_RADIUS_MAX);
/* 458 */     if (isInsideMap(tower.getPosition(), tower.getRadius()) && 
/* 459 */       !collidesWithObstacles(tower.getPosition(), tower.getRadius(), this.obstacles) && 
/* 460 */       !collidesWithTowers(tower.getPosition(), tower.getRadius(), this.towers) && 
/* 461 */       !collidesWithWalkableNodes(tower.getPosition(), tower.getRadius() + maxEnemyRadius, this.walkableNodes)) {
/*     */ 
/*     */       
/* 464 */       for (MapNode n : this.walkableNodes) {
/*     */         
/* 466 */         if (n.getPosition().distance(tower.getPosition()) < (tower.getRadius() + maxEnemyRadius) * 1.1F)
/*     */         {
/* 468 */           for (MapNode neighbor : n.getNeighbors()) {
/* 469 */             if (this.walkableNodes.contains(neighbor)) {
/*     */               
/* 471 */               Point2D midPoint = neighbor.getPosition().midPoint(n.getPosition());
/* 472 */               if (midPoint.distance(tower.getPosition()) < tower.getRadius() + maxEnemyRadius) {
/* 473 */                 return false;
/*     */               }
/*     */             } 
/*     */           } 
/*     */         }
/*     */       } 
/*     */       
/* 480 */       this.towers.add(tower);
/* 481 */       return true;
/*     */     } 
/* 483 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ArrayList<Tower> getTowers() {
/* 491 */     return this.towers;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ArrayList<MapNode> getWalkableNodes() {
/* 499 */     return this.walkableNodes;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Point2D getNodeSize() {
/* 507 */     return new Point2D(this.separation, this.separation);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ArrayList<Enemy> getEnemiesInRange(Point2D position, float range) {
/* 517 */     ArrayList<Enemy> enemiesInRange = new ArrayList<>();
/* 518 */     for (Enemy e : Game.getInstance().getCurrentRound().getAliveEnemies()) {
/* 519 */       if (e.getPosition().distance(position) < range) {
/* 520 */         enemiesInRange.add(e);
/*     */       }
/*     */     } 
/* 523 */     return enemiesInRange;
/*     */   }
/*     */ }


/* Location:              /home/miguel/Universidad/segundo/ADA/practicas/grupoTarde/PracticaTarde3/towerdefense.jar!/net/agsh/towerdefense/Map.class
 * Java compiler version: 20 (64.0)
 * JD-Core Version:       1.1.3
 */