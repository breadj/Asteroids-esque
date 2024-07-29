package Game;

public class GameTime {

    private long previousTime;
    private long currentTime;

    private long deltaTime;

    // milliseconds between model updates
    public final int TICK_RATE;

    public GameTime() {
        TICK_RATE = 15;

        currentTime = System.currentTimeMillis();
        previousTime = currentTime;
    }

    private long delta() {
        return currentTime - previousTime;
    }

    public void update() {
        previousTime = currentTime;
        currentTime = System.currentTimeMillis();

        deltaTime = delta();
    }

    public long lapsed() {
        return deltaTime;
    }

    public double lapsedSecs() {
        return deltaTime / 1000d;
    }
}
