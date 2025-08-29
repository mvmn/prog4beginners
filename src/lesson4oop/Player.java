package lesson4oop;

import java.awt.Color;
import java.awt.Graphics;

public class Player implements GraphicObject {
    private volatile int x = 400;
    public volatile int acceleration = 1;
    public volatile boolean firing = false;

    public int getX() {
        return x;
    }

    public void reset() {
        this.x = 400;
        this.firing = false;
    }

    public void draw(Graphics graphics) {
        graphics.setColor(Color.GREEN);
        graphics.fillOval(x, 575, 10, 10);
        graphics.fillRect(x, 580, 10, 20);

        if (firing) {
            graphics.setColor(Color.YELLOW);
            graphics.fillRect(x + 4, 0, 2, 570);
        }
    }

    public void left() {
        if ((x - acceleration) >= 0) {
            x -= acceleration;
            acceleration++;
        }
    }

    public void right() {
        if ((x + acceleration) <= 800 - 10) {
            x += acceleration;
            acceleration++;
        }
    }
}
