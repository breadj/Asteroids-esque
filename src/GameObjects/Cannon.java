package GameObjects;

import java.awt.*;
import java.awt.geom.AffineTransform;

import Utilities.Vector2D;
import static Game.Constants.gameTime;

public class Cannon extends GameObject {

    // the object the cannon will always attempt to aim at
    private final GameObject target;

    // rotational speed in radians/s
    private final static double TURN_SPEED = Math.PI / 4;
    // how fast the bullets fired are
    private final static int MUZZLE_VELOCITY = 200;
    // constant speed loss factor
    public static final double DRAG = 0.01;
    // starting HP - used to find what fraction of HP is left for the health bar
    private final int totalHP;
    private int hp = 1;
    // the direction the cannon is aiming in
    private Vector2D direction;
    public Bullet bullet = null;


    // coordinates for drawing the Cannon
    private final int[] XP = { -1, -1, -6, -3, 3, 6, 1, 1 };
    private final int[] YP = { 8, 4, 2, -4, -4, 2, 4, 8 };

    public Cannon(Vector2D position, int hitPoints, GameObject target) {
        super(position, new Vector2D(), 21);            // invokes empty constructor of Vector2D, creating a vector of (0, 0) for it's velocity

        totalHP = hitPoints;
        hp = hitPoints;
        direction = Vector2D.normalise(Vector2D.subtract(target.position, position));
        this.target = target;
    }

    // creates a bullet in front of the cannon in the direction it's facing with MUZZLE_VELOCITY speed. Also pushes the cannon back a bit
    private void shoot() {
        bullet = new Bullet(Vector2D.addScaled(position, direction, radius), Vector2D.mult(direction, MUZZLE_VELOCITY), this);
        velocity.addScaled(direction, -15);          // pushes the cannon back (recoil)
    }

    // turns the cannon towards the target based on the TURN_SPEED
    public void turn() {
        double turnTo = Vector2D.angleTo(Vector2D.subtract(target.position, position), direction);

        if ((position.x - target.position.x) * direction.y > (position.y - target.position.y) * direction.x)        // if the cannon has to turn clockwise
            turnTo = Math.min(turnTo, TURN_SPEED * gameTime.lapsedSecs());      // selects the value closer to zero to rotate by
        else            // if the cannon has to turn counter-clockwise
            turnTo = Math.max(turnTo, -TURN_SPEED * gameTime.lapsedSecs());     // selects the value closer to zero to rotate by
        direction.rotate(turnTo);
    }

    @Override
    public void hit() {
        hp--;       // doesn't die when hit, just loses HP
    }

    @Override
    public void collision(GameObject other) {
        // friendly fire from bullets of other cannons can trigger collision with this, but not its own bullets
        if (other instanceof Bullet b && b.getOwner() == this)
            return;
        super.collision(other);
    }

    private int shootTimer = 0;
    @Override
    public void update() {
        if ((shootTimer += gameTime.lapsed()) >= 800) {     // cooldown on shooting is 800ms
            shoot();
            shootTimer = 0;
        }

        turn();

        velocity.mult(1 - DRAG);
        super.update();
    }

    @Override
    public void draw(Graphics2D g) {
        // draws the circle underneath the polygon
        AffineTransform tx1 = g.getTransform();
        g.translate(position.x, position.y);
        g.setColor(Color.DARK_GRAY);
        g.fillOval((int)-radius,(int)-radius, (int)(radius * 2), (int)(radius * 2));
        g.setTransform(tx1);

        // draw the specified polygon
        AffineTransform tx2 = g.getTransform();
        g.translate(position.x, position.y);
        g.rotate(direction.angle() - Math.PI / 2);
        g.scale(4, 4);
        g.setColor(Color.GRAY);
        g.fillPolygon(XP, YP, XP.length);
        g.setTransform(tx2);

        // health bar
        g.setColor(Color.RED);
        g.fillRect((int)(position.x - radius), (int)(position.y + radius), (int)(radius * 2 * ((double)hp / totalHP)), 10);
        g.setColor(Color.WHITE);
        g.drawRect((int)(position.x - radius), (int)(position.y + radius), (int)(radius * 2), 10);
    }

    // only is truly 'dead' once all the HP has depleted
    @Override
    public boolean isDead() {
        return hp <= 0;
    }
}
