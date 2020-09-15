package src.gui;

import java.awt.Polygon;

public class Hexagon extends Polygon {
    private static final long serialVersionUID = 1L;

    public Hexagon(int x, int y, int width, int sideLength, int innerHeight) {
        super();

        addPoint(x + width / 2, y);
        addPoint(x + width, y + innerHeight);
        addPoint(x + width, y + innerHeight + sideLength);
        addPoint(x + width / 2, y + (2 * innerHeight) + sideLength);
        addPoint(x, y + innerHeight + sideLength);
        addPoint(x, y + innerHeight);
    }

}
