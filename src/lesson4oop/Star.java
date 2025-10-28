package lesson4oop;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

public class Star extends GraphicObject {

    private static final Color[] COLORS = {Color.BLUE, Color.CYAN, Color.GREEN, Color.RED, Color.MAGENTA};
    private static final Random random = new Random();
    private final int x;
    private final int y;

    public Star(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void draw(Graphics graphics) {
        graphics.setColor(COLORS[random.nextInt(COLORS.length)]);
        graphics.fillOval(x, y, 6, 6);
    }
}
