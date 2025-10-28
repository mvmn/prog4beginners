package lesson4oop;

import javax.swing.SwingUtilities;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class Game {
    public static final int FIELD_WIDTH = 1024;
    public static final int FIELD_HEIGHT = 768;
    private final MainWindow mainWindow;
    private final Random random = new Random();
    private final Player player = new Player();
    private final Enemy[] enemies = new Enemy[5];
    private volatile int score = 0;
    private volatile int enemySize = 50;
    private volatile int enemySpeed = 1;
    private Star[] stars;

    public volatile int pressedKey = -1;

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

    private Star[] generateStars(int numberOfStars) {
        Star[] result = new Star[numberOfStars];
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
        this.stars = generateStars(100);
        this.player.reset();
        for (int i = 0; i < this.enemies.length; i++) {
            this.enemies[i] = newEnemy();
        }
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
        boolean atLeastOneEnemyWon = false;
        for (Enemy enemy : this.enemies) {
            enemy.move();
            if (enemyWon(enemy)) {
                atLeastOneEnemyWon = true;
                break;
            }
        }
        if (atLeastOneEnemyWon) {
            reset();
        } else {
            if (pressedKey == 37) {
                player.left();
            } else if (pressedKey == 39) {
                player.right();
            } else if (pressedKey == 38) {
                player.setFiring(true);

                int hitIndex = -1;
                for (int i = 0; i < this.enemies.length; i++) {
                    if (this.enemies[i].isHit(player.getFireCoordinate())) {
                        hitIndex = i;
                        break;
                    }
                }

                if (hitIndex>=0) {
                    if (enemySize > 10) {
                        enemySize--;
                        enemySpeed = 1 + (50 - enemySize) / 5;
                    } else {
                        enemySpeed++;
                    }
                    this.enemies[hitIndex] = newEnemy();
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

    private boolean enemyWon(Enemy enemy) {
        return enemy.getY() > FIELD_HEIGHT - 30;
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

    public Enemy[] getEnemies() {
        return this.enemies;
    }

    public int getScore() {
        return this.score;
    }

    public Star[] getStars() {
        return this.stars;
    }
}
