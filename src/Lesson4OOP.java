import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class Lesson4OOP {

    public interface GraphicObject {
        public void draw(Graphics graphics);
    }

    public static class Player implements GraphicObject {
        public volatile int x = 400;
        public volatile int acceleration = 1;
        public volatile boolean firing = false;

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

    public static class Enemy implements GraphicObject {
        public volatile int x;
        public volatile int y;
        public int size;

        public void draw(Graphics graphics) {
            graphics.setColor(Color.RED);
            graphics.fillOval(x, y, size, 10);
        }

        public boolean isHit(int fx) {
            return fx >= x && fx < x + size;
        }
    }

    public static class Star implements GraphicObject {
        public int x;
        public int y;

        public Star(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public void draw(Graphics graphics) {
            graphics.setColor(Color.BLUE);
            graphics.fillOval(x, y, 3, 3);
        }
    }

    public static class Game {
        private final MainWindow mainWindow;
        private final Random random = new Random();
        private Player player = new Player();
        private volatile Enemy enemy;

        private volatile int score = 0;
        private volatile int enemySize = 50;
        private Star[] stars;
        private final int fieldWidth = 800;
        private final int fieldHeight = 600;

        public volatile int pressedKey = -1;

        private volatile int enemyAccel = 1;

        private final Font scoreFont = new Font("SansSerif", Font.BOLD, 36);
        private final AlphaComposite alphaComposite50Percent = AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
                                                                                          0.5f);
        private final AlphaComposite alphaComposite100Percent = AlphaComposite.getInstance(AlphaComposite.SRC_OVER);

        public Game(MainWindow mainWindow) {
            this.mainWindow = mainWindow;
            reset();
        }

        private Star[] generateStars() {
            Random random = new Random();
            Star[] result = new Star[100];
            for (int i = 0; i < result.length; i++) {
                int x = random.nextInt(fieldWidth);
                int y = random.nextInt(fieldHeight);
                result[i] = new Star(x, y);
            }
            return result;
        }

        public void draw(Graphics graphics) {
            Graphics2D g2d = (Graphics2D) graphics;
            g2d.setComposite(alphaComposite100Percent);
            graphics.setColor(Color.BLACK);
            graphics.fillRect(0, 0, graphics.getClipBounds().width, graphics.getClipBounds().height);

            for (Star star : stars) {
                star.draw(graphics);
            }
            player.draw(graphics);
            enemy.draw(graphics);

            graphics.setColor(Color.WHITE);
            g2d.setComposite(alphaComposite50Percent);
            graphics.setFont(scoreFont);
            graphics.drawString(String.valueOf(score), 2, 38);
            Toolkit.getDefaultToolkit().sync();
        }

        private void reset() {
            this.score = 0;
            this.enemySize = 50;
            this.enemy = newEnemy();
            this.stars = generateStars();
            this.player.x = fieldWidth / 2 - 5;
            this.enemyAccel = 1;
        }

        private Enemy newEnemy() {
            Enemy enemy = new Enemy();
            enemy.size = this.enemySize;
            enemy.x = random.nextInt(this.fieldWidth - this.enemySize);
            enemy.y = 0;
            return enemy;
        }

        public void executeStep() throws Exception {
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

                    if (enemy.isHit(player.x + 4)) {
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
                    player.acceleration = 1;
                }
            }
        }
    }

    public static class MainWindow extends JFrame {
        public MainWindow() {
            this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            this.getContentPane().setLayout(new BorderLayout());
        }

        public void start(Game game) {
            JComponent contents = new JComponent() {
                private static final long serialVersionUID = -178839994636617669L;

                @Override
                public void paintComponent(Graphics graphics) {
                    game.draw(graphics);
                }
            };
            contents.setPreferredSize(new Dimension(game.fieldWidth, game.fieldHeight));
            this.getContentPane().add(contents, BorderLayout.CENTER);
            this.pack();
            this.setResizable(false);
            SwingUtilities.invokeLater(() -> this.setVisible(true));
        }
    }

    public static void main(String[] args) throws Exception {
        MainWindow frame = new MainWindow();
        Game game = new Game(frame);
        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == 81) {
                    quit(frame);
                }
                game.pressedKey = e.getKeyCode();
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (game.pressedKey == e.getKeyCode()) {
                    game.pressedKey = -1;
                }
            }
        });
        frame.start(game);

        while (game.pressedKey != 81) {
            game.executeStep();
            frame.repaint();
            Thread.sleep(20);
        }
    }

    private static void quit(JFrame frame) {
        SwingUtilities.invokeLater(() -> {
            frame.setVisible(false);
            frame.dispose();
        });
    }
}
