import java.awt.*;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        /*
        Game game = new Game();
        game.clickSquare(0, 0);
        game.clickSquare(3, 2);

         */

        new MainMenuForm(new Point(0, 0));
        /*
        try {
            new Game().saveLeaderboard();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

         */
    }
}
