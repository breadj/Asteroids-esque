package GameObjects;

import Utilities.Vector2D;

import java.awt.*;

import static Game.Constants.*;

public class Bullet extends GameObject {
    private final Color colour;
    private final GameObject owner;                 // the GameObject that created the bullet
    private final int lifetime;                     // milliseconds of how long it stays on screen before 'dying'
    private int timeAlive = 0;

    public Bullet(Vector2D position, Vector2D velocity, GameObject owner) {
        super(position, velocity, 4);           // radius set to 4 automatically

        this.owner = owner;
        if (owner instanceof Ship)
            colour = Color.GREEN;       // ship shoots green bullets (like a plasma weapon or something)
        else
            colour = Color.RED;         // cannons shoot red bullets because... danger?
        lifetime = 4000;                            // stays on screen for 4 seconds
    }

    public GameObject getOwner() {
        return owner;
    }

    @Override
    public void collision(GameObject other) {
        // so a bullet can't damage the thing that shot it
        if (other != this.owner)
            super.collision(other);
    }

    @Override
    public void update() {
        super.update();

        // if it's been on-screen for 'lifetime' ms then it 'dies' and no longer draws or updates
        if ((timeAlive += gameTime.lapsed()) >= lifetime) {
            dead = true;
            timeAlive = 0;
        }
    }


    @Override
    public void draw(Graphics2D g) {
        g.setColor(colour);
        g.fillOval((int)(position.x - radius), (int)(position.y - radius), (int)radius * 2, (int)radius * 2);
    }
}
