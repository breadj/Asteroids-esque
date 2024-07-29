package GameObjects;

import static Game.Constants.*;

import Input.Action;
import Input.Controller;
import Utilities.Vector2D;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class Ship extends GameObject {

    // rotation velocity in radians/s
    public static final double STEER_RATE = 2 * Math.PI;

    // acceleration when thrust is applied
    public static final double MAG_ACC = 200;

    // constant speed loss factor
    public static final double DRAG = 0.01;

    public static final Color COLOR = Color.CYAN;

    // direction in which nose of ship is pointing
    // this will be in the direction in which thrust is applied
    // it is a unit vector representing the angle the ship is rotated
    public Vector2D direction;

    // controller which provides an Action obj in each frame
    private final Controller ctrl;

    private final int MUZZLE_VELOCITY = 200;
    private int invulnerabilityTimer = 4000;

    public Bullet bullet;

    // polygon points to draw the ship
    private final int[] XP = { -6, 0, 6, 0 };
    private final int[] YP = { 8, 4, 8, -8 };
    private final int[] XTHRUST = { -4, 0, 4, 0 };
    private final int[] YTHRUST = { 6, 2, 6, -6 };

    public Ship(Controller ctrl) {
        // initialises position as the middle of the screen, velocity as no initial movement
        super(new Vector2D(FRAME_WIDTH / 2d, FRAME_HEIGHT / 2d), Vector2D.ZERO, 10);

        this.ctrl = ctrl;
        direction = new Vector2D(0, -1);            // pointing upwards
        bullet = null;
    }

    // creates a bullet from in front of the ship in the direction it's facing with MUZZLE_VELOCITY speed
    public void createBullet() {
        bullet = new Bullet(Vector2D.addScaled(position, direction, radius), Vector2D.mult(direction, MUZZLE_VELOCITY), this);
    }

    // just to use when the ship has been hit but still has lives left in Game
    public void notDead() {
        dead = false;
    }

    @Override
    public void hit() {
        dead = true;

        setInvulnerabilityTimer(2500); // 2.5 seconds of invulnerability
        flash = true;
    }

    public void setInvulnerabilityTimer(int timer) {
        invulnerabilityTimer = timer;
    }

    private int shootTimer = 500;
    @Override
    public void update() {
        if (invulnerabilityTimer > 0) {         // if still in a state of invulnerability
            invulnerabilityTimer -= gameTime.lapsed();
            if (invulnerabilityTimer <= 0) {    // if now the timer has passed
                invulnerabilityTimer = 0;
                flash = false;                  // necessary to make sure the ship doesn't stay invisible after invulnerability has worn off
            }
        }

        Action ac = ctrl.action();              // retrieves the action listener results

        if ((shootTimer += gameTime.lapsed()) >= 500 && ac.shoot) {     // cooldown on shooting is 500ms
            createBullet();
            shootTimer = 0;
        }

        if (ac.turn != 0)
            direction.rotate(ac.turn * STEER_RATE * gameTime.lapsedSecs());
        if (ac.thrust != 0)
            velocity.addScaled(direction, MAG_ACC * ac.thrust * gameTime.lapsedSecs());

        velocity.mult(1 - DRAG);
        super.update();         // updates position by velocity * lapsedSecs and wraps
    }


    @Override
    public void collision(GameObject other) {
        // doesn't check for collision if the ship is still invulnerable
        if (invulnerabilityTimer > 0)
            return;
        // needs to check if the bullet is its own. If it is, don't check collision
        if (other instanceof Bullet b && b.getOwner() == this)
            return;
        super.collision(other);
    }

    private int damageFlash = 200;          // flash time is 200ms
    private boolean flash = false;
    @Override
    public void draw(Graphics2D g) {        // flashes visible and invisible while invulnerable
        if (invulnerabilityTimer > 0) {
            if ((damageFlash -= gameTime.lapsed()) <= 0) {
                damageFlash = 200;
                flash = !flash;             // inverts the flash from visible->invisible / invisible->visible
            }
        }
        if (flash)
            return;

        AffineTransform tx = g.getTransform();
        g.translate(position.x, position.y);
        g.rotate(direction.angle() + Math.PI / 2);
        g.scale(2, 2);
        g.setColor(COLOR);
        g.fillPolygon(XP, YP, XP.length);

        // adds smaller red ship icon over normal ship when thrust is active
        if (ctrl.action().thrust == 1) {
            g.setColor(Color.RED);
            g.fillPolygon(XTHRUST, YTHRUST, XP.length);
        }
        g.setTransform(tx);
    }
}
