import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GameWindow {
    public static final int WIDTH = 1060;
    public static final int HEIGHT = 640;
    private JPanel mainPanel;
    private final Difficulty difficulty;
    private final JLabel[][] labels;
    private final Game game;
    private final JFrame frame;

    public GameWindow(Difficulty difficulty, Point location) {
        this.frame = new JFrame(Game.TITLE);
        this.frame.setLayout(new BorderLayout());
        this.frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.frame.setSize(GameWindow.WIDTH, GameWindow.HEIGHT);
        this.labels = new JLabel[difficulty.getHeight()][difficulty.getWidth()];

        this.difficulty = difficulty;
        this.addImageLabels();

        this.frame.setVisible(true);
        this.frame.setLocation(location);

        this.game = new Game(difficulty, this);

        JPanel buttonPanel = new JPanel();
        this.frame.add(buttonPanel, BorderLayout.SOUTH);

        JButton saveGameButton = new JButton("Save Game");
        buttonPanel.add(saveGameButton);

        JButton mainMenuButton = new JButton("Main Menu");
        buttonPanel.add(mainMenuButton);

        saveGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GameWindow.this.saveGame();
            }
        });

        mainMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                new MainMenuForm(location);
            }
        });

        this.run();
    }

    public void run() {
        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int row = (int)((e.getLocationOnScreen().getY() - GameWindow.this.difficulty.getIndentationY()) / Square.SIZE);
                int column = (int)((e.getLocationOnScreen().getX() - GameWindow.this.difficulty.getIndentationX()) / Square.SIZE);
                GameWindow.this.changeLabelIcon(row, column, GameWindow.this.game.getSquare(row, column));
                // System.out.println(e.getX() + " , " + e.getY());
            }
        };

        this.frame.addMouseListener(mouseAdapter);

    }

    public void changeLabelIcon(int row, int column, Square square) {
        ImageIcon imageIcon;

        if (square.isFlagged()) {
            imageIcon = new ImageIcon(".\\images\\flag.png");
        } else if (square.getSquareStatus() == SquareStatus.MINE) {
            imageIcon = new ImageIcon(".\\images\\exploded_mine.png");
        } else if (square.getSquareStatus() == SquareStatus.EMPTY) {
            imageIcon = new ImageIcon(".\\images\\empty.png");
        } else {
            imageIcon = new ImageIcon(".\\images\\" + square.getNearbyMines() + ".png");
        }

        this.labels[row][column].setIcon(imageIcon);
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

    private void addImageLabels() {
        for (int i = 0; i < this.difficulty.getHeight(); i++) {
            for (int j = 0; j < this.difficulty.getWidth(); j++) {
                JLabel imageLabel = new JLabel(new ImageIcon(".\\images\\unclicked.png"));
                int x = this.difficulty.getIndentationX() + j * Square.SIZE;
                int y = this.difficulty.getIndentationY() + i * Square.SIZE;
                imageLabel.setBounds(this.difficulty.getIndentationX() + j * Square.SIZE,  this.difficulty.getIndentationY() + i * Square.SIZE, Square.SIZE, Square.SIZE);
                this.labels[i][j] = imageLabel;
                this.frame.add(imageLabel, BorderLayout.CENTER);
            }
        }
    }
}
