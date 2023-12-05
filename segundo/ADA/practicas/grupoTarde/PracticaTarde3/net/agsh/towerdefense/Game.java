package net.agsh.towerdefense;

import net.agsh.towerdefense.gui.GameWindow;

import java.io.PrintStream;

public class Game {
  private static Game instance;
  private Random random = null;
  private long elapsed = 0L;
  private long remaining = 0L;
  private float timeScaleFactor = 1.0F;
  private Map map;
  private Round currentRound;
  private GameWindow gameWindow;
  private final Config config = new Config();
  private int score = 0;
  private boolean isPaused = false;

  public Game() {
  }

  public static Game getInstance() {
    if (instance == null) {
      instance = new Game();
    }

    return instance;
  }

  public int getVersion() {
    return 231121;
  }

  public Map getMap() {
    return this.map;
  }

  public Round getCurrentRound() {
    return this.currentRound;
  }

  public void init(long seed) {
    this.random = new Random(seed);
    this.map = new Map(new Point2D(this.config.get(Config.Parameter.MAP_SIZE_X), this.config.get(Config.Parameter.MAP_SIZE_Y)), this.config.get(Config.Parameter.MAP_GRID_SPACE));
    this.map.init();
    this.random.pushSeed();
    this.currentRound = new Round();
    if (this.config.get(Config.Parameter.GUI) != 0.0F) {
      this.gameWindow = new GameWindow();
    }

  }

  public void update(long millis) {
    long stepSize = (long)this.config.get(Config.Parameter.GAME_STEP_SIZE);

    long delta;
    for(delta = millis + this.remaining; delta >= stepSize; delta -= stepSize) {
      this.elapsed += stepSize;
      this.currentRound.update(stepSize);
      if (this.currentRound.ended()) {
        this.score += this.currentRound.getScore();
        delta = 0L;
        PrintStream var10000 = System.out;
        int var10001 = this.currentRound.getRoundNumber();
        var10000.println("Round " + var10001 + " score: " + this.currentRound.getScore());
        if (!this.gameOver()) {
          this.map = new Map(new Point2D(this.config.get(Config.Parameter.MAP_SIZE_X), this.config.get(Config.Parameter.MAP_SIZE_Y)), this.config.get(Config.Parameter.MAP_GRID_SPACE));
          this.map.init();
          this.random.popSeed();
          this.random.pushSeed();
          this.currentRound = new Round();
        }
      }
    }

    this.remaining = delta;
  }

  public void play() {
    this.elapsed = 0L;
    this.remaining = 0L;
    long stepSize = (long)this.config.get(Config.Parameter.GAME_STEP_SIZE);
    if (this.config.get(Config.Parameter.GUI) != 0.0F) {
      long delta = 0L;
      long lastTime = System.currentTimeMillis();
      long sleepTime = 0L;

      while(!this.gameOver()) {
        delta = System.currentTimeMillis() - lastTime;
        lastTime = System.currentTimeMillis();
        if (!this.isPaused) {
          this.update((long)((float)delta * this.timeScaleFactor));
          if (delta < stepSize) {
            sleepTime = stepSize - delta;

            try {
              Thread.sleep(sleepTime);
            } catch (InterruptedException var11) {
              var11.printStackTrace();
            }
          }
        } else {
          sleepTime = 100L;

          try {
            Thread.sleep(sleepTime);
          } catch (InterruptedException var10) {
            var10.printStackTrace();
          }
        }
      }

      this.gameWindow.dispose();
    } else {
      while(!this.gameOver()) {
        this.update(stepSize);
      }
    }

  }

  public boolean gameOver() {
    return this.currentRound.ended() && (float)this.currentRound.getRoundNumber() == this.config.get(Config.Parameter.GAME_MAX_ROUNDS) - 1.0F;
  }

  public long getElapsedTime() {
    return this.elapsed;
  }

  Random getRandom() {
    return this.random;
  }

  public int getScore() {
    return this.score;
  }

  public float getParam(Config.Parameter parameter) {
    return this.config.get(parameter);
  }

  public void setParam(Config.Parameter parameter, float v) {
    if (this.random == null) {
      this.config.set(parameter, v);
    } else {
      throw new RuntimeException("Cannot set parameter after the game has been initialized");
    }
  }

  public boolean isPaused() {
    return this.isPaused;
  }

  public void pause() {
    this.isPaused = true;
  }

  public void resume() {
    this.isPaused = false;
  }

  public void finish() {
    this.random = null;
  }


  public void setTimeScale(float timeScaleFactor) {
    this.timeScaleFactor = timeScaleFactor;
  }

}
