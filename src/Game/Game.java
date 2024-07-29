package Game;

import GameObjects.Cannon;
import GameObjects.GameObject;
import GameObjects.Ship;
import Input.Keys;
import Utilities.JEasyFrame;
import Utilities.Vector2D;

import java.util.ArrayList;
import java.util.List;

import static Game.Constants.*;

public class Game {
    public List<GameObject> objects;
    public Keys ctrl;
    public Ship ship;

    private int score = 0;
    private int lives = 3;
    private boolean hasDied = false;
    public int level = 1;
    private int cannons = 0;

    public boolean complete = false;

    public Game() {
        objects = new ArrayList<>();

        ctrl = new Keys();
        ship = new Ship(ctrl);
        objects.add(ship);
    }

    public static void main(String[] args)
        throws Exception {
        Game game = new Game();
        newLevel(game);
        View view = new View(game);
        new JEasyFrame(view, "Basic Game").addKeyListener(game.ctrl);

        while (!game.complete) {
            if (game.getCannonCount() == 0) {
                if (game.level == 5) {
                    game.complete = true;
                    break;
                }
                game.level++;
                newLevel(game);
            }
            gameTime.update();
            game.update();
            view.repaint();
            Thread.sleep(gameTime.TICK_RATE);
        }

        // You Win screen

    }

    // modifies the game argument by creating a new ship and loading all the respective levels cannon placements
    public static void newLevel(Game game) {
        game.objects.clear();

        game.ship = new Ship(game.ctrl);
        game.objects.add(game.ship);

        Level newLevel = Level.getLevels()[game.level - 1];         // have to -1 as changing from level to array index (starts at 0)
        int[] hpValues = distributeHP(newLevel.getHPPool(), newLevel.getCannonPositions().length);        // retrieves the properly distributed HP for the cannons
        for (int i = 0; i < newLevel.getCannonPositions().length; i++)
            game.objects.add(new Cannon(new Vector2D(newLevel.getCannonPositions()[i]), hpValues[i], game.ship));    // adds all cannons to the GameObjects list
        game.cannons = newLevel.getCannonPositions().length;
    }

    // randomly distributes X amount of HP between N number of cannons using random floats and a cumulative normalising denominator
    private static int[] distributeHP(int hpPool, int noOfCannons) {
        // allocate ALL cannons at LEAST 1hp - taking away 1 for each cannon then adding at the end
        hpPool -= noOfCannons;
        int[] cannonHPValues = new int[noOfCannons];

        double[] distHP = new double[noOfCannons];
        double denominator = 0;         // cumulatively adds up the random values to get a denominator that can normalise the values
        for (int i = 0; i < noOfCannons; i++) {
            double rand = Math.random();
            distHP[i] = hpPool * rand;
            denominator += rand;
        }
        for (int i = 0; i < noOfCannons; i++)
            cannonHPValues[i] = 1 + (int)Math.round(distHP[i] / denominator);         // normalises HP by using a common denominator

        return cannonHPValues;
    }

    // removes all dead objects
    private void corpseCollection() {
        objects.removeIf(GameObject::isDead);
    }

    private int currentScore = 0;
    public void update() {
        int previousScore = currentScore;
        currentScore = score;
        // hasDied check makes sure you can't get the extra life bonus if you died in the level you got another 5000 points
        if (previousScore != currentScore && currentScore % 5000 == 0 && currentScore > 0 && !hasDied)
            lives++;

        int cannonCount = 0;
        for (int i = 0; i < objects.size(); i++) {
            for (int j = i + 1; j < objects.size(); j++) {
                objects.get(i).collision(objects.get(j));
            }

            if (objects.get(i).equals(ship)) {
                // only truly dies if all lives are gone
                if (ship.isDead()) {
                    lives--;
                    score -= 500;       // penalty for dying is -500 points
                    hasDied = true;
                    if (lives > 0)
                        ship.notDead();
                }
            } else if (objects.get(i) instanceof Cannon cannon) {
                cannonCount++;

                // if the cannon has fired a bullet, add that to the object list and remove it from the cannon (so it's free to fire another bullet)
                if (cannon.bullet != null) {
                    objects.add(cannon.bullet);
                    cannon.bullet = null;
                }

                // rewards 200 points for destroying a cannon
                if (cannon.isDead())
                    incScore(200);
            }
        }
        cannons = cannonCount;      // cannon count to check if all cannons in the level have been destroyed, and if so, move onto the next level

        objects.forEach(GameObject::update);

        // if the ship has fired a bullet, add that to the object list and remove it from the ship (so it's free to fire another bullet)
        if (ship.bullet != null) {
            objects.add(ship.bullet);
            ship.bullet = null;
        }

        synchronized (Game.class) {
            corpseCollection();     // removes all dead objects
        }
    }

    public void incScore(int add) {
        score += add;
    }

    public int getScore() {
        return score;
    }

    public int getLives() {
        return lives;
    }

    public int getLevel() {
        return level;
    }

    public int getCannonCount() {
        return cannons;
    }
}
