package Input;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Keys extends KeyAdapter implements Controller {
    Action action;

    public Keys() {
        action = new Action();
    }

    public Action action() {
        return action;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_UP -> action.thrust = 1;
            case KeyEvent.VK_LEFT -> action.turnLeft();
            case KeyEvent.VK_RIGHT -> action.turnRight();
            case KeyEvent.VK_SPACE -> action.shoot = true;
            case KeyEvent.VK_ESCAPE -> System.exit(0);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_UP -> action.thrust = 0;
            case KeyEvent.VK_LEFT -> action.turnRight();
            case KeyEvent.VK_RIGHT -> action.turnLeft();
            case KeyEvent.VK_SPACE -> action.shoot = false;
        }
    }
}
