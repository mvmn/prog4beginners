package lesson4oop;

import javax.swing.SwingUtilities;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class Game {
    public static final int FIELD_WIDTH = 800;
    public static final int FIELD_HEIGHT = 600;
    private final MainWindow mainWindow;
    private final Random random = new Random();
    private final Player player = new Player();
    private volatile Enemy enemy;
    private volatile int score = 0;
    private volatile int enemySize = 50;
    private Star[] stars;

    public volatile int pressedKey = -1;

    private volatile int enemySpeed = 1;

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
            int x = random.nextInt(FIELD_WIDTH);
            int y = random.nextInt(FIELD_HEIGHT);
            result[i] = new Star(x, y);
        }
        return result;
    }

    private void reset() {
        this.score = 0;
        this.enemySpeed = 1;
        this.enemySize = 50;
        this.stars = generateStars();
        this.player.reset();
        this.enemy = newEnemy();
    }

    private Enemy newEnemy() {
        int x = random.nextInt(this.FIELD_WIDTH - this.enemySize);
        return new Enemy(this.enemySize, x, enemySpeed);
    }

    public boolean executeStep() throws Exception {
        if (pressedKey == 81) {
            quit();
            return false;
        }
        enemy.move();
        if (enemy.getY() > FIELD_HEIGHT - 30) {
            reset();
        } else {
            if (pressedKey == 37) {
                player.left();
            } else if (pressedKey == 39) {
                player.right();
            } else if (pressedKey == 38) {
                player.setFiring(true);

                if (enemy.isHit(player.getX() + 4)) {
                    if (enemySize > 10) {
                        enemySize--;
                        enemySpeed = 1 + (50 - enemySize) / 5;
                    } else {
                        enemySpeed++;
                    }
                    enemy = newEnemy();
                    score++;
                }

                mainWindow.repaint();
                Thread.sleep(20);

                player.setFiring(false);
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

    public Player getPlayer() {
        return this.player;
    }

    public Enemy getEnemy() {
        return this.enemy;
    }

    public int getScore() {
        return this.score;
    }

    public Star[] getStars() {
        return this.stars;
    }
}
