import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;

public class GameWindow implements Serializable {
    public static final int WIDTH = 1060;
    public static final int HEIGHT = 640;
    private JPanel mainPanel;
    private final Difficulty difficulty;
    private final JLabel[][] labels;
    private final Game game;
    private final JFrame frame;
    private final JButton saveGameButton;
    private final JButton mainMenuButton;

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

        this.saveGameButton = new JButton("Save Game");
        buttonPanel.add(this.saveGameButton);

        this.mainMenuButton = new JButton("Main Menu");
        buttonPanel.add(this.mainMenuButton);

        this.run();
    }

    public void run() {
        this.frame.setVisible(true);

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
                new MainMenuForm(GameWindow.this.frame.getLocation());
            }
        });

        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                boolean pressedLeftButton = e.getButton() == 1;
                boolean pressedRightButton = e.getButton() == 3;

                double upperBar = 30;
                double mouseLocationX = e.getLocationOnScreen().getX() - GameWindow.this.frame.getLocation().getX();
                double mouseLocationY = e.getLocationOnScreen().getY() - GameWindow.this.frame.getLocation().getY() - upperBar;

                int row = (int)((mouseLocationY - GameWindow.this.difficulty.getIndentationY()) / Square.SIZE);
                int column = (int)((mouseLocationX - GameWindow.this.difficulty.getIndentationX()) / Square.SIZE);

                if (pressedLeftButton) {
                    GameWindow.this.game.clickSquare(row, column);

                    if (GameWindow.this.game.getGameStatus() == GameStatus.WIN) {
                        JOptionPane.showMessageDialog(null,"You WON!");
                        GameWindow.this.end();
                    } else if (GameWindow.this.game.getGameStatus() == GameStatus.LOSS) {
                        JOptionPane.showMessageDialog(null, "You LOST!");
                        GameWindow.this.end();
                    }
                } else if (pressedRightButton) {
                    GameWindow.this.game.flagSquare(row, column);
                }
            }
        };

        this.frame.addMouseListener(mouseAdapter);
    }

    public void changeLabelIconLeftClick(int row, int column, Square square) {
        ImageIcon imageIcon;

        if (square.getSquareStatus() == SquareStatus.MINE) {
            imageIcon = new ImageIcon(".\\images\\exploded_mine.png");
        } else if (square.getSquareStatus() == SquareStatus.EMPTY) {
            imageIcon = new ImageIcon(".\\images\\empty.png");
        } else {
            imageIcon = new ImageIcon(".\\images\\" + square.getNearbyMines() + ".png");
        }

        this.labels[row][column].setIcon(imageIcon);
    }

    public void changeLabelIconRightClick(int row, int column, Square square) {
        Icon icon = this.labels[row][column].getIcon();

        if (!square.isClicked() && square.isFlagged()) {
            icon = new ImageIcon(".\\images\\flag.png");
        } else if (!square.isClicked()) {
            icon = new ImageIcon(".\\images\\unclicked.png");
        }

        this.labels[row][column].setIcon(icon);
    }

    public void end() {
        this.frame.dispose();
        new MainMenuForm(this.frame.getLocation());
    }

    private void saveGame() {
        if (!this.game.isSaved()) {
            String[] options = new String[]{"Save Anyway", "Back to the Game"};
            int chosedOption = JOptionPane.showOptionDialog(null, "If you save your progress now you won't be able to get to the leaderboard. Do you wish to continue?", "Click a button", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
            if (chosedOption == 0) {
                this.chooseSaveSlot();
            }
        } else {
            this.chooseSaveSlot();
        }
    }

    private void chooseSaveSlot() {
        String[] saveSlots = new String[]{"Save Slot 1", "Save Slot 2", "Save Slot 3"};
        int chosedSaveSlot = JOptionPane.showOptionDialog(null, "Choose the save slot where your game will be saved. If there is already saved game, it will be overridden.", "Click a button", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, saveSlots, saveSlots[0]);

        if (chosedSaveSlot != -1) {
            boolean saved = true;
            this.game.saveGame(chosedSaveSlot + 1);

            try {
                FileOutputStream fileOutputStream = new FileOutputStream(".\\saves\\save_slot" + (chosedSaveSlot + 1) + ".sav");
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                objectOutputStream.writeObject(this);
                objectOutputStream.close();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "An error occured while saving the game.");
                saved = false;
            }

            if (saved) {
                this.game.setSaved(true);
                JOptionPane.showMessageDialog(null, "Game saved.");
            }
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
                this.frame.add(imageLabel);
            }
        }
    }
}
