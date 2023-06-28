import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameWindowForm {
    private JPanel mainPanel;
    private JButton saveGameButton;
    private JButton mainMenuButton;
    private final Game game;

    public GameWindowForm(Difficulty difficulty) {
        JFrame frame = new JFrame(Game.TITLE);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setContentPane(this.mainPanel);
        frame.pack();
        frame.setVisible(true);

        this.game = new Game(difficulty);

        this.saveGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GameWindowForm.this.saveGame();
            }
        });

        this.mainMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                new MainMenuForm();
            }
        });
    }

    private void saveGame() {
        String[] options = new String[]{"Save Anyway", "Back to the Game"};
        int chosedOption = JOptionPane.showOptionDialog(null, "If you save your progress now you won't be able to get to the leaderboard. Do you wish to continue?", "Click a button", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

        if (chosedOption == 0) {
            this.game.setSaved(true);
        }
    }
}
