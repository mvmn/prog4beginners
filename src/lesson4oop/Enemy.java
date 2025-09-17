package lesson4oop;

import java.awt.Color;
import java.awt.Graphics;

public class Enemy extends GraphicObject {
    private final int x;
    private volatile int y = 0;
    private final int size;
    private final int speed;

    public Enemy(int size, int x, int speed) {
        this.size = size;
        this.x = x;
        this.speed = speed;
    }

    public void draw(Graphics graphics) {
        graphics.setColor(Color.RED);
        graphics.fillOval(x, y, size, 10);
    }

    public boolean isHit(int fx) {
        return fx >= x && fx < x + size;
    }

    public int getY() {
        return this.y;
    }

    public void move() {
        this.y += speed;
    }
}
