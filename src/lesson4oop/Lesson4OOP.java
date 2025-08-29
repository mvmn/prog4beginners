package lesson4oop;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Lesson4OOP {

    public static void main(String[] args) throws Exception {
        MainWindow frame = new MainWindow();
        Game game = new Game(frame);
        frame.start(game);

        while (game.executeStep()) {
            frame.repaint();
            Thread.sleep(20);
        }
    }
}
