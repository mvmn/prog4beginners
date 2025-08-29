package lesson4oop;

import java.awt.Color;
import java.awt.Graphics;

public class Enemy implements GraphicObject {
    private final int x;
    public volatile int y = 0;
    private final int size;

    public Enemy(int size, int x) {
        this.size = size;
        this.x = x;
    }

    public void draw(Graphics graphics) {
        graphics.setColor(Color.RED);
        graphics.fillOval(x, y, size, 10);
    }

    public boolean isHit(int fx) {
        return fx >= x && fx < x + size;
    }
}
