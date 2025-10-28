package lesson4oop;

import java.awt.Color;
import java.awt.Graphics;

public class Enemy extends GraphicObject {
    private volatile int x;
    private volatile int y = 0;
    private final int size;
    private final int speed;

    private volatile int offset = 10;

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

        if(this.x+offset>=0 && this.x+offset+size<800) {
            this.x += offset;
        }
        if(offset>0) {
            offset-=1;
        } else if(offset<-1) {
            offset+=1;
        } else if(offset==0) {
            offset = -11;
        } else if(offset==-1) {
            offset = 10;
        }
    }
}
