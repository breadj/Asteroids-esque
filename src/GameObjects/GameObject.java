package GameObjects;

import Utilities.Vector2D;

import java.awt.*;

import static Game.Constants.*;

public abstract class GameObject {
    protected Vector2D position;        // per frame
    protected Vector2D velocity;        // per second
    protected boolean dead;
    protected final double radius;

    GameObject(Vector2D position, Vector2D velocity, double radius) {
        this.position = position;
        this.velocity = velocity;
        this.radius = radius;

        dead = false;
    }

    public void hit() {
        dead = true;
    }
    public void update() {
        position.addScaled(velocity, gameTime.lapsedSecs());
        position.wrap(FRAME_WIDTH, FRAME_HEIGHT);
    }
    public abstract void draw(Graphics2D g);
    public boolean isDead() {
        return dead;
    }


    public boolean intersects(GameObject other) {
        return (this.radius + other.radius) > position.dist(other.position);
    }

    public void collision(GameObject other) {
        if (!this.isDead() && this.getClass() != other.getClass() && this.intersects(other)) {
            this.hit();
            other.hit();
        }
    }
}
