package lesson4oop;

import javax.swing.SwingUtilities;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class Game {
    private final MainWindow mainWindow;
    private final Random random = new Random();
    public Player player = new Player();
    public volatile Enemy enemy;

    public volatile int score = 0;
    private volatile int enemySize = 50;
    public Star[] stars;
    public final int fieldWidth = 800;
    public final int fieldHeight = 600;

    public volatile int pressedKey = -1;

    private volatile int enemyAccel = 1;

    public Game(MainWindow mainWindow) {
        this.mainWindow = mainWindow;

        mainWindow.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                pressedKey = e.getKeyCode();
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (pressedKey == e.getKeyCode()) {
                    pressedKey = -1;
                }
            }
        });
        reset();
    }

    private Star[] generateStars() {
        Star[] result = new Star[100];
        for (int i = 0; i < result.length; i++) {
            int x = random.nextInt(fieldWidth);
            int y = random.nextInt(fieldHeight);
            result[i] = new Star(x, y);
        }
        return result;
    }

    private void reset() {
        this.score = 0;
        this.enemySize = 50;
        this.enemy = newEnemy();
        this.stars = generateStars();
        this.player.reset();
        this.enemyAccel = 1;
    }

    private Enemy newEnemy() {
        int x = random.nextInt(this.fieldWidth - this.enemySize);
        return new Enemy(this.enemySize, x);
    }

    public boolean executeStep() throws Exception {
        if (pressedKey == 81) {
            quit();
            return false;
        }
        enemy.y += enemyAccel;
        if (enemy.y > fieldHeight - 30) {
            reset();
        } else {
            if (pressedKey == 37) {
                player.left();
            } else if (pressedKey == 39) {
                player.right();
            } else if (pressedKey == 38) {
                player.firing = true;

                if (enemy.isHit(player.getX() + 4)) {
                    enemy = newEnemy();
                    if (enemySize > 10) {
                        enemySize--;
                        enemyAccel = 1 + (50 - enemySize) / 5;
                    } else {
                        enemyAccel++;
                    }
                    score++;
                }

                mainWindow.repaint();
                Thread.sleep(20);

                player.firing = false;
            } else {
                player.stop();
            }
        }
        return true;
    }

    private void quit() {
        SwingUtilities.invokeLater(() -> {
            mainWindow.setVisible(false);
            mainWindow.dispose();
        });
    }
}
