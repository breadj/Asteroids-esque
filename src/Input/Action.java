package Input;

public class Action {
    public int thrust;      // 0 = off, 1 = on
    public int turn;        // -1 = counter-clockwise turn, 0 = no turn, 1 = clockwise turn
    public boolean shoot;


    public void turnLeft() {
        if (turn > -1)
            turn -= 1;
        else turn = -1;
    }

    public void turnRight() {
        if (turn < 1)
            turn += 1;
        else turn = 1;
    }
}
