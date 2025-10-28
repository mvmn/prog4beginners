package lesson4oop;

import java.awt.Color;
import java.awt.Graphics;

public class Player extends GraphicObject {
    private volatile int x = Game.FIELD_WIDTH / 2;
    private volatile int acceleration = 1;
    private volatile boolean firing = false;

    private final int width = 10;

    public int getFireCoordinate() {
        return x + width / 2;
    }

    public void reset() {
        this.x = 400;
        this.firing = false;
    }

    public void draw(Graphics graphics) {
        graphics.setColor(Color.GREEN);
        graphics.fillOval(x, Game.FIELD_HEIGHT - 25, width, 10);
        graphics.fillRect(x, Game.FIELD_HEIGHT - 20, width, 20);

        if (firing) {
            graphics.setColor(Color.YELLOW);
            graphics.fillRect(x + width / 2 - 1, 0, 2, Game.FIELD_HEIGHT - 30);
        }
    }

    public void left() {
        if ((x - acceleration) >= 0) {
            x -= acceleration;
            acceleration++;
        }
    }

    public void right() {
        if ((x + acceleration) <= Game.FIELD_WIDTH - 10) {
            x += acceleration;
            acceleration++;
        }
    }

    public void stop() {
        acceleration = 1;
    }

    public void setFiring(boolean firing) {
        this.firing = firing;
    }
}
