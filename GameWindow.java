import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameWindow {
    public static final int WIDTH = 1060;
    public static final int HEIGHT = 640;
    private JPanel mainPanel;
    private final JButton saveGameButton;
    private final JButton mainMenuButton;
    private final Difficulty difficulty;
    private final JLabel[][] labels;
    private final Game game;

    public GameWindow(Difficulty difficulty, Point location) {
        JFrame frame = new JFrame(Game.TITLE);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(GameWindow.WIDTH, GameWindow.HEIGHT);
        this.labels = new JLabel[difficulty.getHeight()][difficulty.getWidth()];

        this.difficulty = difficulty;
        this.addImageLabels(frame);

        frame.setVisible(true);
        frame.setLocation(location);

        this.game = new Game(difficulty, this);

        JPanel buttonPanel = new JPanel();
        frame.add(buttonPanel, BorderLayout.SOUTH);

        this.saveGameButton = new JButton("Save Game");
        buttonPanel.add(this.saveGameButton, BorderLayout.SOUTH);

        this.mainMenuButton = new JButton("Main Menu");
        buttonPanel.add(this.mainMenuButton, BorderLayout.SOUTH);

        this.saveGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GameWindow.this.saveGame();
            }
        });

        this.mainMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                new MainMenuForm(location);
            }
        });
    }

    public void changeLabel(int row, int column) {
        this.labels[row][column].setIcon(new ImageIcon(".\\images\\flag.png"));
    }

    private void saveGame() {
        if (!this.game.isSaved()) {
            String[] options = new String[]{"Save Anyway", "Back to the Game"};
            int chosedOption = JOptionPane.showOptionDialog(null, "If you save your progress now you won't be able to get to the leaderboard. Do you wish to continue?", "Click a button", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

            if (chosedOption == 0) {
                this.game.setSaved(true);
                JOptionPane.showMessageDialog(null, "Game saved.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Game saved.");
        }
    }

    private void addImageLabels(JFrame frame) {
        for (int i = 0; i < this.difficulty.getHeight(); i++) {
            for (int j = 0; j < this.difficulty.getWidth(); j++) {
                JLabel imageLabel = new JLabel(new ImageIcon(".\\images\\unclicked.png"));
                Dimension size = imageLabel.getPreferredSize();
                imageLabel.setBounds(this.difficulty.getIndentationX() + j * Square.SIZE,  this.difficulty.getIndentationY() + i * Square.SIZE, size.width, size.height);
                this.labels[i][j] = imageLabel;
                frame.add(imageLabel);
            }
        }
    }
}
