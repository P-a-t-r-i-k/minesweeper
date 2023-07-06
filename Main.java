import java.awt.*;

public class Main {
    private static GameWindow gameWindow;
    public static void main(String[] args) {
        new MainMenuForm(new Point(0, 0));
        Main.gameWindow.run();

    }

    public static void setGameWindow(GameWindow gameWindow) {
        Main.gameWindow = gameWindow;
    }
}
