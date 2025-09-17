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

public class Lesson4Procedural {

    static volatile int keyPressed = -1;
    static volatile int playerX;
    static volatile int fireX;
    static volatile int enemySize;
    static volatile int enemyX;
    static volatile int enemyY;
    static volatile int playerAccel = 1;
    static volatile int enemySpeed = 1;
    static volatile int score = 0;

    static final Font scoreFont = new Font("SansSerif", Font.BOLD, 36);
    static final AlphaComposite alphaComposite50Percent = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f);
    static final AlphaComposite alphaComposite100Percent = AlphaComposite.getInstance(AlphaComposite.SRC_OVER);


    public static void main(String[] args) throws Exception {
        final int fieldWidth = 800;
        final int fieldHeight = 600;
        playerX = fieldWidth / 2 - 5;
        fireX = -1;

        enemySize = 50;
        Random random = new Random();
        enemyX = random.nextInt(fieldWidth - enemySize);
        enemyY = 0;

        final int numStars = 100;
        int[] starsX = new int[numStars];
        int[] starsY = new int[numStars];
        generateStars(fieldWidth, fieldHeight, random, numStars, starsX, starsY);

        JFrame mainWindow = new JFrame();
        mainWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        mainWindow.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                keyPressed = e.getKeyCode();
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (keyPressed == e.getKeyCode()) {
                    keyPressed = -1;
                }
            }
        });

        JComponent contents = new JComponent() {
            private static final long serialVersionUID = -178839994636617669L;

            @Override
            public void paintComponent(Graphics graphics) {
                draw(graphics, numStars, starsX, starsY);
            }
        };
        contents.setPreferredSize(new Dimension(fieldWidth, fieldHeight));

        mainWindow.getContentPane().setLayout(new BorderLayout());
        mainWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        mainWindow.getContentPane().add(contents, BorderLayout.CENTER);
        mainWindow.pack();
        mainWindow.setResizable(false);
        SwingUtilities.invokeLater(() -> mainWindow.setVisible(true));

        while (true) {
            enemyY += enemySpeed;
            if (enemyY > fieldHeight - 30) {
                enemySpeed = resetGame(fieldWidth, random);
                generateStars(fieldWidth, fieldHeight, random, numStars, starsX, starsY);
            }
            int key = keyPressed;
            if (key > 0) {
                if (key == 81) {
                    close(mainWindow);
                    return;
                }
                if (key == 37 && (playerX - playerAccel) >= 0) {
                    playerX -= playerAccel;
                    playerAccel++;
                } else if (key == 39 && (playerX + playerAccel) <= fieldWidth - 10) {
                    playerX += playerAccel;
                    playerAccel++;
                } else if (key == 38) {
                    if (fire(fieldWidth, random, mainWindow)) {
                        nextEnemy(fieldWidth, random);
                        score++;
                    }
                }
            } else {
                playerAccel = 1;
            }
            mainWindow.repaint();
            Thread.sleep(20);
        }
    }

    private static void nextEnemy(int fieldWidth, Random random) {
        enemyX = random.nextInt(fieldWidth - enemySize);
        enemyY = 0;
        if (enemySize > 10) {
            enemySize--;
            enemySpeed = 1 + (50 - enemySize) / 5;
        } else {
            enemySpeed++;
        }
    }

    private static boolean fire(int fieldWidth, Random random, JFrame frame) throws InterruptedException {
        fireX = playerX + 4;

        boolean hit = fireX >= enemyX && fireX < enemyX + enemySize;

        frame.repaint();
        Thread.sleep(20);
        fireX = -1;

        return hit;
    }

    private static int resetGame(int fieldWidth, Random random) {
        int enemyAccel;
        score = 0;
        enemyX = (random.nextInt(fieldWidth - enemySize));
        enemyY = 0;
        playerX = fieldWidth / 2 - 5;
        enemySize = 50;
        enemyAccel = 1;
        return enemyAccel;
    }

    private static void draw(Graphics graphics, int numStars, int[] starsX, int[] starsY) {
        Graphics2D g2d = (Graphics2D) graphics;
        drawBackground(graphics, g2d);
        drawStars(g2d, numStars, starsX, starsY);
        drawPlayer(g2d);
        drawEnemy(g2d);
        drawScore(g2d);
        Toolkit.getDefaultToolkit().sync();
    }

    private static void drawBackground(Graphics graphics, Graphics2D g2d) {
        g2d.setComposite(alphaComposite100Percent);
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, graphics.getClipBounds().width, graphics.getClipBounds().height);
    }

    private static void drawStars(Graphics2D graphics, int numStars, int[] starsX, int[] starsY) {
        graphics.setColor(Color.BLUE);
        for (int i = 0; i < numStars; i++) {
            graphics.fillOval(starsX[i], starsY[i], 3, 3);
        }
    }

    private static void drawPlayer(Graphics2D graphics) {
        graphics.setColor(Color.GREEN);
        graphics.fillOval(playerX, 575, 10, 10);
        graphics.fillRect(playerX, 580, 10, 20);

        if (fireX >= 0) {
            graphics.setColor(Color.YELLOW);
            graphics.fillRect(fireX, 0, 2, 570);
        }
    }

    private static void drawEnemy(Graphics2D graphics) {
        graphics.setColor(Color.RED);
        graphics.fillOval(enemyX, enemyY, enemySize, 10);
    }

    private static void drawScore(Graphics2D g2d) {
        g2d.setColor(Color.WHITE);
        g2d.setComposite(alphaComposite50Percent);
        g2d.setFont(scoreFont);
        g2d.drawString(String.valueOf(score), 2, 38);
    }

    private static void generateStars(int fieldWidth, int fieldHeight, Random random, int numStars, int[] starsX, int[] starsY) {
        for (int i = 0; i < numStars; i++) {
            starsX[i] = random.nextInt(fieldWidth);
            starsY[i] = random.nextInt(fieldHeight);
        }
    }

    private static void close(JFrame frame) {
        SwingUtilities.invokeLater(() -> {
            frame.setVisible(false);
            frame.dispose();
        });
    }
}
