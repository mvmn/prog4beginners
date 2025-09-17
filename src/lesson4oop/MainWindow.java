package lesson4oop;

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

public class MainWindow extends JFrame {

    private final Font scoreFont = new Font("SansSerif", Font.BOLD, 36);
    private final AlphaComposite alphaComposite50Percent = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f);
    private final AlphaComposite alphaComposite100Percent = AlphaComposite.getInstance(AlphaComposite.SRC_OVER);

    public MainWindow() {
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.getContentPane().setLayout(new BorderLayout());
    }

    public void start(Game game) {
        JComponent contents = new JComponent() {
            private static final long serialVersionUID = -178839994636617669L;

            @Override
            public void paintComponent(Graphics graphics) {
                Graphics2D g2d = (Graphics2D) graphics;
                g2d.setComposite(alphaComposite100Percent);
                graphics.setColor(Color.BLACK);
                graphics.fillRect(0, 0, graphics.getClipBounds().width, graphics.getClipBounds().height);

                GraphicObject[] objectsToDraw = new GraphicObject[game.getStars().length + 3];
                int i = 0;
                for (Star star : game.getStars()) {
                    objectsToDraw[i++] = star;
                    // star.draw(graphics);
                }
                objectsToDraw[i++] = game.getPlayer();
                objectsToDraw[i++] = game.getEnemy();
                objectsToDraw[i++] = game.getEnemy2();

                for (GraphicObject objectToDraw : objectsToDraw) {
                    objectToDraw.draw(graphics);
                }

                graphics.setColor(Color.WHITE);
                g2d.setComposite(alphaComposite50Percent);
                graphics.setFont(scoreFont);
                graphics.drawString(String.valueOf(game.getScore()), 2, 38);
                Toolkit.getDefaultToolkit().sync();
            }
        };
        contents.setPreferredSize(new Dimension(game.FIELD_WIDTH, game.FIELD_HEIGHT));
        this.getContentPane().add(contents, BorderLayout.CENTER);
        this.pack();
        this.setResizable(false);
        SwingUtilities.invokeLater(() -> this.setVisible(true));
    }
}
