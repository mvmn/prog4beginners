import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
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

    public static void main(String[] args) throws Exception {
        final int fieldWidth = 800;
        final int fieldHeight = 600;
        AtomicInteger keyPressed = new AtomicInteger();
        AtomicInteger playerX = new AtomicInteger(fieldWidth / 2 - 5);
        int playerAccel = 1;
        AtomicInteger fireX = new AtomicInteger(-1);

        AtomicInteger enemySize = new AtomicInteger(50);
        Random random = new Random();
        AtomicInteger enemyX = new AtomicInteger(random.nextInt(fieldWidth - enemySize.get()));
        AtomicInteger enemyY = new AtomicInteger(0);
        int enemyAccel = 1;

        final int numStars = 100;
        int[] starsX = new int[numStars];
        int[] starsY = new int[numStars];
        for (int i = 0; i < numStars; i++) {
            starsX[i] = random.nextInt(fieldWidth);
            starsY[i] = random.nextInt(fieldHeight);
        }

        AtomicInteger score = new AtomicInteger(0);

        JFrame frame = new JFrame();
        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                keyPressed.set(e.getKeyCode());
            }

            @Override
            public void keyReleased(KeyEvent e) {
                keyPressed.compareAndSet(e.getKeyCode(), 0);
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

                graphics.fillOval(playerX.get(), 575, 10, 10);
                graphics.fillRect(playerX.get(), 580, 10, 20);

                int fx = fireX.get();
                if (fx >= 0) {
                    graphics.setColor(Color.YELLOW);
                    graphics.fillRect(fx, 0, 2, 570);
                }

                graphics.setColor(Color.RED);
                graphics.fillOval(enemyX.get(), enemyY.get(), enemySize.get(), 10);

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
        frame.setVisible(true);

        while (true) {
            if (enemyY.addAndGet(enemyAccel) > fieldHeight - 30) {
                score.set(0);
                enemyX.set(random.nextInt(fieldWidth - enemySize.get()));
                enemyY.set(0);
                playerX.set(fieldWidth / 2 - 5);
                enemySize.set(50);
                enemyAccel = 1;
                for (int i = 0; i < numStars; i++) {
                    starsX[i] = random.nextInt(fieldWidth);
                    starsY[i] = random.nextInt(fieldHeight);
                }
            }
            int key = keyPressed.get();
            if (key > 0) {
                if (key == 81) {
                    quit(frame);
                    return;
                }
                int x = playerX.get();
                if (key == 37 && (x - playerAccel) >= 0) {
                    playerX.addAndGet(-playerAccel);
                    playerAccel++;
                } else if (key == 39 && (x + playerAccel) <= fieldWidth - 10) {
                    playerX.addAndGet(playerAccel);
                    playerAccel++;
                } else if (key == 38) {
                    fireX.set(x + 4);

                    int ex = enemyX.get();
                    int fx = fireX.get();
                    if (fx >= ex && fx < ex + enemySize.get()) {
                        enemyX.set(random.nextInt(fieldWidth - enemySize.get()));
                        enemyY.set(0);
                        if (enemySize.get() > 10) {
                            enemyAccel = 1 + (50 - enemySize.decrementAndGet()) / 5;
                        } else {
                            enemyAccel++;
                        }
                        score.incrementAndGet();
                    }

                    frame.repaint();
                    Thread.sleep(20);
                    fireX.set(-1);
                } else {
                    //playerAccel = 1;
                }
            } else {
                playerAccel = 1;
            }
            frame.repaint();
            Thread.sleep(20);
        }
    }

    private static void quit(JFrame frame) {
        frame.setVisible(false);
        frame.dispose();
    }
}
