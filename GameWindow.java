import javax.swing.JLabel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.Timer;
import java.awt.Point;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;

public class GameWindow implements Serializable {
    public static final int WIDTH = 1170;
    public static final int HEIGHT = 640;
    private final Difficulty difficulty;
    private final JLabel[][] labels;
    private final Game game;
    private final JFrame frame;
    private final JButton saveGameButton;
    private final JButton mainMenuButton;
    private int gameTime;
    private final NumberDisplay flagDisplay;
    private final JLabel[] flagLabels;
    private final NumberDisplay timeDisplay;
    private final JLabel[] timeLabels;

    public GameWindow(Difficulty difficulty, Point location) {
        this.frame = new JFrame(Game.TITLE);
        this.frame.setLayout(new BorderLayout());
        this.frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.frame.setSize(GameWindow.WIDTH, GameWindow.HEIGHT);
        this.labels = new JLabel[difficulty.getHeight()][difficulty.getWidth()];

        this.difficulty = difficulty;
        this.game = new Game(difficulty, this);

        this.flagDisplay = new NumberDisplay();
        this.flagLabels = new JLabel[NumberDisplay.DISPLAY_SIZE];
        this.timeDisplay = new NumberDisplay();
        this.timeLabels = new JLabel[NumberDisplay.DISPLAY_SIZE];
        this.addImageLabels();

        this.frame.setVisible(true);
        this.frame.setLocation(location);

        JPanel buttonPanel = new JPanel();
        this.frame.add(buttonPanel, BorderLayout.SOUTH);

        this.saveGameButton = new JButton("Save Game");
        buttonPanel.add(this.saveGameButton);

        this.mainMenuButton = new JButton("Main Menu");
        buttonPanel.add(this.mainMenuButton);

        this.gameTime = 0;
        this.run();
    }

    public void run() {
        this.frame.setVisible(true);

        ActionListener updateClockAction = e -> {
            GameWindow.this.gameTime++;
            GameWindow.this.changeNumberLabels(GameWindow.this.gameTime, GameWindow.this.timeLabels, GameWindow.this.timeDisplay);
        };

        Timer timer = new Timer(1000, updateClockAction);
        timer.start();

        this.saveGameButton.addActionListener(e -> GameWindow.this.saveGame());

        this.mainMenuButton.addActionListener(e -> {
            timer.stop();
            this.frame.dispose();
            new MainMenuForm(GameWindow.this.frame.getLocation());
        });

        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                boolean pressedLeftButton = e.getButton() == 1;
                boolean pressedRightButton = e.getButton() == 3;

                double upperBar = 30;
                double leftSideBar = 55;
                double mouseLocationX = e.getLocationOnScreen().getX() - GameWindow.this.frame.getLocation().getX() - leftSideBar;
                double mouseLocationY = e.getLocationOnScreen().getY() - GameWindow.this.frame.getLocation().getY() - upperBar;

                int row = (int)((mouseLocationY - GameWindow.this.difficulty.getIndentationY()) / Square.SIZE);
                int column = (int)((mouseLocationX - GameWindow.this.difficulty.getIndentationX()) / Square.SIZE);

                if (pressedLeftButton) {
                    GameWindow.this.game.clickSquare(row, column);

                    if (GameWindow.this.game.getGameStatus() == GameStatus.WIN) {
                        timer.stop();
                        JOptionPane.showMessageDialog(null, "You WON!");
                        if (!GameWindow.this.game.isSaved()) {
                            GameWindow.this.game.changeLeaderboard();
                        }
                        GameWindow.this.end();
                    } else if (GameWindow.this.game.getGameStatus() == GameStatus.LOSS) {
                        timer.stop();
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
        this.changeNumberLabels(this.game.getFlagsLeft(), this.flagLabels, this.flagDisplay);
    }

    public void changeLabelIconRightClick(int row, int column, Square square) {
        Icon icon = this.labels[row][column].getIcon();

        if (!square.isClicked() && square.isFlagged()) {
            icon = new ImageIcon(".\\images\\flag.png");
        } else if (!square.isClicked() && !square.isFlagged()) {
            icon = new ImageIcon(".\\images\\unclicked.png");
        }

        this.labels[row][column].setIcon(icon);
        this.changeNumberLabels(this.game.getFlagsLeft(), this.flagLabels, this.flagDisplay);
    }

    public void end() {
        this.frame.dispose();
        new MainMenuForm(this.frame.getLocation());
    }

    public int getGameTime() {
        return this.gameTime;
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
        JPanel leftSidePanel = new JPanel();
        this.frame.add(leftSidePanel, BorderLayout.WEST);

        JPanel timePanel = new JPanel(new GridLayout(1, 3));
        leftSidePanel.add(timePanel, BorderLayout.NORTH);

        for (int i = 0; i < 3; i++) {
            JLabel jLabel = new JLabel(new ImageIcon(".\\images\\timer_0.png"));
            timePanel.add(jLabel);
            this.timeLabels[i] = jLabel;
        }


        JPanel rightSidePanel = new JPanel();
        this.frame.add(rightSidePanel, BorderLayout.EAST);

        JPanel flagsLeftPanel = new JPanel(new GridLayout(1, 3));
        rightSidePanel.add(flagsLeftPanel, BorderLayout.CENTER);

        for (int i = 0; i < 3; i++) {
            JLabel jLabel = new JLabel();
            flagsLeftPanel.add(jLabel);
            this.flagLabels[i] = jLabel;
        }
        this.changeNumberLabels(this.game.getFlagsLeft(), this.flagLabels, this.flagDisplay);


        JPanel labelPanel = new JPanel(null);
        this.frame.add(labelPanel);

        for (int i = 0; i < this.difficulty.getHeight(); i++) {
            for (int j = 0; j < this.difficulty.getWidth(); j++) {
                JLabel imageLabel = new JLabel(new ImageIcon(".\\images\\unclicked.png"));
                imageLabel.setBounds(this.difficulty.getIndentationX() + j * Square.SIZE,  this.difficulty.getIndentationY() + i * Square.SIZE, Square.SIZE, Square.SIZE);
                this.labels[i][j] = imageLabel;
                labelPanel.add(imageLabel);
            }
        }
    }

    private void changeNumberLabels(int newNumber, JLabel[] labels, NumberDisplay numberDisplay) {
        numberDisplay.changeNumber(newNumber);

        for (int i = 0; i < labels.length; i++) {
            labels[i].setIcon(new ImageIcon(".\\images\\timer_" + numberDisplay.getFigure(i) + ".png"));
        }
    }
}
