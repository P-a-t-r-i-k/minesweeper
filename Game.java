import javax.swing.JOptionPane;
import java.io.Serializable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.EOFException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class Game implements Serializable {
    public static final String TITLE = "Minesweeper";
    public static final String LEADERBOARD_FILE = ".\\leaderboard.ldr";
    public static final int VERSION = 1;
    public static final int LEADERBOARD_SIZE = 5;
    private final Square[][] squares;
    private GameStatus gameStatus;
    private final Difficulty difficulty;
    private int hiddenSquares;
    private boolean saved;
    private final ArrayList<LeaderboardEntry> leaderboard;
    private final GameWindow gameWindow;
    private int flagsLeft;

    public Game(Difficulty difficulty, GameWindow gameWindow) {
        this.gameStatus = GameStatus.UNFINISHED;
        this.gameWindow = gameWindow;

        this.difficulty = difficulty;
        this.hiddenSquares = difficulty.getHeight() * difficulty.getWidth();

        this.squares = new Square[difficulty.getHeight()][difficulty.getWidth()];
        this.createMines(this.createIndexesOfSquaresWithMines());
        this.addNumbersToSquares();

        this.saved = false;
        this.leaderboard = new ArrayList<>();

        this.flagsLeft = this.difficulty.getMines();
    }

    public void clickSquare(int row, int column) {
        if (row < 0 || row >= this.squares.length) {
            return;
        }
        if (column < 0 || column >= this.squares[row].length) {
            return;
        }

        if (!this.squares[row][column].isClicked()) {
            this.squares[row][column].setClicked(true);
            this.hiddenSquares--;
            this.addFlagIfRemovedByLeftClick(this.squares[row][column]);
            this.gameWindow.changeLabelIconLeftClick(row, column, this.squares[row][column]);

            if (this.squares[row][column].getSquareStatus() == SquareStatus.EMPTY) {
                this.showNearbyEmptySquares(this.squares[row][column]);
            } else if (this.squares[row][column].getSquareStatus() == SquareStatus.MINE) {
                this.gameStatus = GameStatus.LOSS;
            }

            if (this.hiddenSquares == this.difficulty.getMines()) {
                this.gameStatus = GameStatus.WIN;
            }
        }
    }

    public void flagSquare(int row, int column) {
        if (row < 0 || row >= this.squares.length) {
            return;
        }
        if (column < 0 || column >= this.squares[row].length) {
            return;
        }

        Square square = this.squares[row][column];
        if (square.isClicked()) {
            return;
        } else if (!square.isFlagged() && this.flagsLeft > 0) {
            square.setFlagged(true);
            this.flagsLeft--;
        } else if (square.isFlagged()) {
            square.setFlagged(false);
            this.flagsLeft++;
        }

        this.gameWindow.changeLabelIconRightClick(row, column, this.squares[row][column]);
    }

    public boolean isSaved() {
        return this.saved;
    }

    public void setSaved(boolean saved) {
        this.saved = saved;
    }

    public GameStatus getGameStatus() {
        return this.gameStatus;
    }

    public int getFlagsLeft() {
        return this.flagsLeft;
    }

    public void saveGame(int saveSlot) {
        System.out.println(saveSlot);
    }

    private Difficulty getDifficulty() {
        return this.difficulty;
    }

    private void createMines(int[] indexesOfSquaresWithMines) {
        int index = 0;
        int position = 0;

        for (int i = 0; i < this.squares.length; i++) {
            for (int j = 0; j < this.squares[i].length; j++) {
                SquareStatus squareStatus = SquareStatus.EMPTY;

                if (index == indexesOfSquaresWithMines[position]) {
                    squareStatus = SquareStatus.MINE;

                    position++;
                    if (position == indexesOfSquaresWithMines.length) {
                        position = indexesOfSquaresWithMines.length - 1;
                    }
                }

                this.squares[i][j] = new Square(i, j, squareStatus);
                index++;
            }
        }
    }

    private int[] createIndexesOfSquaresWithMines() {
        int[] indexesOfSquaresWithMines = new int[this.difficulty.getMines()];
        ArrayList<Integer> usedIndexes = new ArrayList<>();
        Random random = new Random();


        for (int i = 0; i < indexesOfSquaresWithMines.length; i++) {
            int randomIndex;
            do {
                randomIndex = random.nextInt(this.squares.length * this.squares[0].length);
            } while (usedIndexes.contains(randomIndex));

            indexesOfSquaresWithMines[i] = randomIndex;
            usedIndexes.add(randomIndex);
        }

        Arrays.sort(indexesOfSquaresWithMines);

        return indexesOfSquaresWithMines;
    }

    private void addNumbersToSquares() {
        for (int i = 0; i < this.squares.length; i++) {
            for (int j = 0; j < this.squares[i].length; j++) {
                this.squares[i][j].setNearbyMines(this.countNearbyMines(this.squares[i][j]));
                if (this.squares[i][j].getNearbyMines() > 0) {
                    this.squares[i][j].setSquareStatus(SquareStatus.NUMBER);
                }
            }
        }
    }

    private int countNearbyMines(Square square) {
        int minesCount = 0;
        if (square.getSquareStatus() == SquareStatus.MINE) {
            return minesCount;
        }

        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if (square.getRow() + i < 0 || square.getRow() + i >= this.squares.length) {
                    continue;
                }
                if (square.getColumn() + j < 0 || square.getColumn() + j >= this.squares[0].length) {
                    continue;
                }
                if (this.squares[square.getRow() + i][square.getColumn() + j].getSquareStatus() == SquareStatus.MINE) {
                    minesCount++;
                }
            }
        }

        return minesCount;
    }

    private void showNearbyEmptySquares(Square square) {
        ArrayList<Square> squaresToCheck = new ArrayList<>();
        squaresToCheck.add(square);

        while (!squaresToCheck.isEmpty()) {
            Square currentSquare = squaresToCheck.get(0);
            squaresToCheck.remove(currentSquare);

            if (!currentSquare.isClicked()) {
                currentSquare.setClicked(true);
                this.gameWindow.changeLabelIconLeftClick(currentSquare.getRow(), currentSquare.getColumn(), currentSquare);
                this.addFlagIfRemovedByLeftClick(square);
                this.hiddenSquares--;
            }

            this.addNearbyEmptySquares(currentSquare, squaresToCheck);
        }
    }

    private void addNearbyEmptySquares(Square currentSquare, ArrayList<Square> squaresToCheck) {
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if (currentSquare.getRow() + i < 0 || currentSquare.getRow() + i >= this.squares.length) {
                    continue;
                }
                if (currentSquare.getColumn() + j < 0 || currentSquare.getColumn() + j >= this.squares[0].length) {
                    continue;
                }
                if (this.squares[currentSquare.getRow() + i][currentSquare.getColumn() + j].isClicked()) {
                    continue;
                }
                if (this.squares[currentSquare.getRow() + i][currentSquare.getColumn() + j].getSquareStatus() == SquareStatus.EMPTY) {
                    if (!squaresToCheck.contains(this.squares[currentSquare.getRow() + i][currentSquare.getColumn() + j])) {
                        squaresToCheck.add(this.squares[currentSquare.getRow() + i][currentSquare.getColumn() + j]);
                    }
                } else if (this.squares[currentSquare.getRow() + i][currentSquare.getColumn() + j].getSquareStatus() == SquareStatus.NUMBER) {
                    this.squares[currentSquare.getRow() + i][currentSquare.getColumn() + j].setClicked(true);
                    this.addFlagIfRemovedByLeftClick(this.squares[currentSquare.getRow() + i][currentSquare.getColumn() + j]);
                    this.gameWindow.changeLabelIconLeftClick(currentSquare.getRow() + i, currentSquare.getColumn() + j, this.squares[currentSquare.getRow() + i][currentSquare.getColumn() + j]);
                    this.hiddenSquares--;
                }
            }
        }
    }

    public void changeLeaderboard() {
        int timeBonus = this.getTimeBonus();
        int currentScore = this.getDifficulty().getDifficultyBonus() + timeBonus;

        try {
            this.loadLeaderboard();
        } catch (IOException e) {

        }

        int minScoreInLeaderboard;
        if (this.leaderboard.size() == 0) {
            minScoreInLeaderboard = 0;
        } else {
            minScoreInLeaderboard = this.leaderboard.get(this.leaderboard.size() - 1).getScore();
        }

        if (this.leaderboard.size() < Game.LEADERBOARD_SIZE || currentScore > minScoreInLeaderboard) {
            this.addLeaderboardEntry(currentScore);
        }

        try {
            this.saveLeaderboard();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadLeaderboard() throws IOException {
        FileInputStream fileInputStream = new FileInputStream(new File(Game.LEADERBOARD_FILE));
        DataInputStream dataInputStream = new DataInputStream(fileInputStream);
        this.leaderboard.clear();

        int version = dataInputStream.readInt();
        if (version == 1) {
            boolean reachedEndOfFile = false;
            while (!reachedEndOfFile) {
                try {
                    String player = dataInputStream.readUTF();
                    int score = dataInputStream.readInt();
                    this.leaderboard.add(new LeaderboardEntry(player, score));
                } catch (EOFException e) {
                    reachedEndOfFile = true;
                }
            }
        }

        Collections.sort(this.leaderboard);
        Collections.reverse(this.leaderboard);
    }

    private void addLeaderboardEntry(int score) {
        if (this.leaderboard.size() == Game.LEADERBOARD_SIZE) {
            this.leaderboard.remove(this.leaderboard.size() - 1);
        }

        String playerName = JOptionPane.showInputDialog("You reached score " + score + " and placed yourself in the leaderboard. Please enter your name:");
        if (playerName == null) {
            playerName = "Unknown";
        }

        this.leaderboard.add(new LeaderboardEntry(playerName, score));
        Collections.sort(this.leaderboard);
        Collections.reverse(this.leaderboard);
    }

    private void saveLeaderboard() throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(new File(Game.LEADERBOARD_FILE));
        DataOutputStream dataOutputStream = new DataOutputStream(fileOutputStream);

        dataOutputStream.writeInt(Game.VERSION);
        for (LeaderboardEntry leaderboardEntry : this.leaderboard) {
            dataOutputStream.writeUTF(leaderboardEntry.getName());
            dataOutputStream.writeInt(leaderboardEntry.getScore());
        }
    }

    private int getTimeBonus() {
        int maxGameTimeToGetBonus = 1000;
        int bonus = maxGameTimeToGetBonus - this.gameWindow.getGameTime();

        if (bonus < 0) {
            bonus = 0;
        }

        return bonus;
    }

    private void addFlagIfRemovedByLeftClick(Square square) {
        if (square.isFlagged()) {
            this.flagsLeft++;
        }
    }
}
