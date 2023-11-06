package analyzer;

/**
 * A class that keeps track of time.
 */
public class Chronometer {
    public long startTime, pausedTime, pauseTime, resumeTime, stopTime;
    public boolean paused, stopped;

    /**
     * Creates a new chronometer.
     */
    public Chronometer() {
        start();
    }

    /**
     * Starts the chronometer.
     */
    public void start() {
        startTime = System.currentTimeMillis();
        pausedTime = pauseTime = resumeTime = stopTime = 0;
        paused = stopped = false;
    }

    /**
     * Pauses the chronometer.
     */
    public void pause() {
        if (!paused && !stopped) {
            pauseTime = System.currentTimeMillis();
            paused = true;
        } else {
            throw new IllegalStateException("analyzer.Chronometer is already paused or stopped.");
        }
    }

    /**
     * Resumes the chronometer.
     */
    public void resume() {
        if (paused && !stopped) {
            resumeTime = System.currentTimeMillis();
            //elapsedTime += resumeTime - pauseTime;
            pausedTime += resumeTime - pauseTime;
            paused = false;
        } else {
            throw new IllegalStateException("analyzer.Chronometer is not paused or is stopped.");
        }
    }

    /**
     * Stops the chronometer.
     */
    public void stop() {
        if (!stopped && !paused) {
            stopTime = System.currentTimeMillis();
            stopped = true;
        } else {
            throw new IllegalStateException("analyzer.Chronometer is already stopped or paused.");
        }
    }

    /**
     * Returns the elapsed time.
     * @return the elapsed time
     */
    public long getElapsedTime() {
        if (paused) {
            return pauseTime - startTime - pausedTime;
        } else if (stopped) {
            return stopTime - startTime - pausedTime;
        } else {
            return System.currentTimeMillis() - startTime - pausedTime;
        }
    }

    public static void main(String[] args) {
        System.out.println("Create chronometer");
        Chronometer chrono = new Chronometer();
        System.out.println(chrono.getElapsedTime() + " (0)\nStart chronometer");
        chrono.start();
        System.out.println(chrono.getElapsedTime() + " (0)\nWait 1s");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(chrono.getElapsedTime() + " (1000)\nPause chronometer");
        chrono.pause();
        System.out.println(chrono.getElapsedTime() + " (1000)\nWait 1s");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(chrono.getElapsedTime() + " (1000)\nResume chronometer");
        chrono.resume();
        System.out.println(chrono.getElapsedTime() + " (1000)\nWait 1s");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(chrono.getElapsedTime() + " (2000)\nPause chronometer");
        chrono.pause();
        System.out.println(chrono.getElapsedTime() + " (2000)\nWait 1s");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(chrono.getElapsedTime() + " (2000)\nResume chronometer");
        chrono.resume();
        System.out.println(chrono.getElapsedTime() + " (2000)\nWait 1s");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(chrono.getElapsedTime() + " (3000)\nStop chronometer");
        chrono.stop();
        System.out.println(chrono.getElapsedTime() + " (3000)");
    }
}
