package lesson4oop;

import java.awt.Color;
import java.awt.Graphics;

public class Star implements GraphicObject {
    private final int x;
    private final int y;

    public Star(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void draw(Graphics graphics) {
        graphics.setColor(Color.BLUE);
        graphics.fillOval(x, y, 3, 3);
    }
}
