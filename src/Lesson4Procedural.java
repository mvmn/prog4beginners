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
import java.util.concurrent.atomic.AtomicInteger;

public class Lesson4Procedural {

    static volatile int keyPressed = -1;
    static volatile int playerX;
    static volatile int fireX;
    static volatile int enemySize;
    static volatile int enemyX;
    static volatile int enemyY;
    static volatile int playerAccel = 1;


    public static void main(String[] args) throws Exception {
        final int fieldWidth = 800;
        final int fieldHeight = 600;
        playerX = fieldWidth / 2 - 5;
        fireX = -1;

        enemySize = 50;
        Random random = new Random();
        enemyX = random.nextInt(fieldWidth - enemySize);
        enemyY = 0;
        int enemyAccel = 1;

        final int numStars = 100;
        int[] starsX = new int[numStars];
        int[] starsY = new int[numStars];
        generateStars(fieldWidth, fieldHeight, random, numStars, starsX, starsY);

        AtomicInteger score = new AtomicInteger(0);

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                keyPressed = e.getKeyCode();
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (keyPressed == e.getKeyCode()) {
                    keyPressed = 0;
                }
            }
        });
        frame.getContentPane().setLayout(new BorderLayout());

        final Font scoreFont = new Font("SansSerif", Font.BOLD, 36);
        AlphaComposite alphaComposite50Percent = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f);
        AlphaComposite alphaComposite100Percent = AlphaComposite.getInstance(AlphaComposite.SRC_OVER);

        JComponent contents = new JComponent() {
            private static final long serialVersionUID = -178839994636617669L;

            @Override
            public void paintComponent(Graphics graphics) {
                Graphics2D g2d = (Graphics2D) graphics;
                g2d.setComposite(alphaComposite100Percent);
                graphics.setColor(Color.BLACK);
                graphics.fillRect(0, 0, graphics.getClipBounds().width, graphics.getClipBounds().height);

                graphics.setColor(Color.BLUE);
                for (int i = 0; i < numStars; i++) {
                    graphics.fillOval(starsX[i], starsY[i], 3, 3);
                }

                graphics.setColor(Color.GREEN);
                graphics.fillOval(playerX, 575, 10, 10);
                graphics.fillRect(playerX, 580, 10, 20);

                int fx = fireX;
                if (fx >= 0) {
                    graphics.setColor(Color.YELLOW);
                    graphics.fillRect(fx, 0, 2, 570);
                }

                graphics.setColor(Color.RED);
                graphics.fillOval(enemyX, enemyY, enemySize, 10);

                graphics.setColor(Color.WHITE);
                g2d.setComposite(alphaComposite50Percent);
                graphics.setFont(scoreFont);
                graphics.drawString(String.valueOf(score.get()), 2, 38);
                Toolkit.getDefaultToolkit().sync();
            }
        };

        contents.setPreferredSize(new Dimension(fieldWidth, fieldHeight));

        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().add(contents, BorderLayout.CENTER);
        frame.pack();
        frame.setResizable(false);
        SwingUtilities.invokeLater(() -> frame.setVisible(true));

        while (true) {
            enemyY += enemyAccel;
            if (enemyY > fieldHeight - 30) {
                score.set(0);
                enemyX = (random.nextInt(fieldWidth - enemySize));
                enemyY = (0);
                playerX = fieldWidth / 2 - 5;
                enemySize = (50);
                enemyAccel = 1;
                generateStars(fieldWidth, fieldHeight, random, numStars, starsX, starsY);
            }
            int key = keyPressed;
            if (key > 0) {
                if (key == 81) {
                    quit(frame);
                    return;
                }
                if (key == 37 && (playerX - playerAccel) >= 0) {
                    playerX -= playerAccel;
                    playerAccel++;
                } else if (key == 39 && (playerX + playerAccel) <= fieldWidth - 10) {
                    playerX += playerAccel;
                    playerAccel++;
                } else if (key == 38) {
                    fireX = playerX + 4;

                    int ex = enemyX;
                    int fx = playerX + 4;
                    if (fx >= ex && fx < ex + enemySize) {
                        enemyX = random.nextInt(fieldWidth - enemySize);
                        enemyY = 0;
                        if (enemySize > 10) {
                            enemySize--;
                            enemyAccel = 1 + (50 - enemySize) / 5;
                        } else {
                            enemyAccel++;
                        }
                        score.incrementAndGet();
                    }

                    frame.repaint();
                    Thread.sleep(20);
                    fireX = -1;
                }
            } else {
                playerAccel = 1;
            }
            frame.repaint();
            Thread.sleep(20);
        }
    }

    private static void generateStars(int fieldWidth, int fieldHeight, Random random, int numStars, int[] starsX, int[] starsY) {
        for (int i = 0; i < numStars; i++) {
            starsX[i] = random.nextInt(fieldWidth);
            starsY[i] = random.nextInt(fieldHeight);
        }
    }

    private static void quit(JFrame frame) {
        SwingUtilities.invokeLater(() -> {
            frame.setVisible(false);
            frame.dispose();
        });
    }
}
