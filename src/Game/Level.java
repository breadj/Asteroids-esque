package Game;

import Utilities.Vector2D;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;

public class Level implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    private int hpPool;
    private Vector2D[] cannonPositions;

    public Level() {}

    public void setHPPool(final int hpPool) {
        this.hpPool = hpPool;
    }

    public void setCannonPositions(final Vector2D[] cannonPositions) {
        this.cannonPositions = cannonPositions;
    }

    public int getHPPool() {
        return hpPool;
    }

    public Vector2D[] getCannonPositions() {
        return cannonPositions;
    }

    // creates all five levels and returns an array of them
    public static Level[] getLevels() {
        Level[] levels = new Level[5];

        Vector2D[] l1Positions = { new Vector2D(320, 360), new Vector2D(960, 360) };
        levels[0] = new Level(); levels[0].setHPPool(3); levels[0].setCannonPositions(l1Positions);

        Vector2D[] l2Positions = { new Vector2D(640, 160), new Vector2D(450, 480), new Vector2D(830, 480) };
        levels[1] = new Level(); levels[1].setHPPool(5); levels[1].setCannonPositions(l2Positions);

        Vector2D[] l3Positions = { new Vector2D(240, 360), new Vector2D(400, 360), new Vector2D(840, 360), new Vector2D(1040, 360) };
        levels[2] = new Level(); levels[2].setHPPool(9); levels[2].setCannonPositions(l3Positions);

        Vector2D[] l4Positions = { new Vector2D(80, 80), new Vector2D(1200, 80), new Vector2D(640, 240), new Vector2D(80, 640), new Vector2D(1200, 640) };
        levels[3] = new Level(); levels[3].setHPPool(12); levels[3].setCannonPositions(l4Positions);

        Vector2D[] l5Positions = { new Vector2D(256, 180), new Vector2D(1024, 180), new Vector2D(320, 240), new Vector2D(960, 240), new Vector2D(288, 360),
                new Vector2D(992, 360), new Vector2D(320, 480), new Vector2D(960, 480), new Vector2D(256, 540), new Vector2D(1024, 540) };
        levels[4] = new Level(); levels[4].setHPPool(28); levels[4].setCannonPositions(l5Positions);


        /*try {
            FileOutputStream fos = new FileOutputStream("levels.xml");
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            XMLEncoder encoder = new XMLEncoder(bos);
            encoder.writeObject(levels);
            encoder.close();
        } catch (Exception e) {
            System.out.println("Fuck.");
        }*/

        return levels;
    }

    // parses the levels.xml file and populates the levels array with all the levels stored in the XML file
    public static Level[] getXMLLevels() {
        try {
            FileInputStream levelsFile = new FileInputStream("levels.xml");
            BufferedInputStream buffFile = new BufferedInputStream(levelsFile);
            XMLDecoder decoder = new XMLDecoder(buffFile);
            Level[] levels = (Level[])decoder.readObject();
            return levels;
        } catch (Exception e) {
            e.printStackTrace();
            return getLevels();         // hard-coded levels JUST IN CASE levels.xml doesn't work
        }
    }
}
