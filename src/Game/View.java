package Game;

import javax.swing.*;
import java.awt.*;
import static Game.Constants.*;

public class View extends JComponent {
    public static final Color BG_COLOR = Color.black;

    private final Game game;

    public View(Game game) {
        this.game = game;
    }

    @Override
    public void paintComponent(Graphics gr) {
        Graphics2D g = (Graphics2D)gr;

        // paint the background
        g.setColor(BG_COLOR);
        g.fillRect(0, 0, getWidth(), getHeight());

        synchronized (Game.class) {
            game.objects.forEach(gameObject -> gameObject.draw(g));
        }

        // draws level number and score in the top left corner
        g.setColor(Color.WHITE);
        g.drawString("Level: " + game.getLevel(), 5, g.getFontMetrics().getHeight());
        g.drawString("Score: " + game.getScore(), 5, g.getFontMetrics().getHeight() * 2);

        // draws number of lives and asteroids remaining in the rop right corner
        String livesString = "Lives: " + game.getLives();
        g.drawString(livesString, FRAME_WIDTH - g.getFontMetrics().stringWidth(livesString), g.getFontMetrics().getHeight());
        String asteroidsString = "Cannons: " + game.getCannonCount();
        g.drawString(asteroidsString, FRAME_WIDTH - g.getFontMetrics().stringWidth(asteroidsString), g.getFontMetrics().getHeight() * 2);
    }

    @Override
    public Dimension getPreferredSize() {
        return Constants.FRAME_SIZE;
    }
}
