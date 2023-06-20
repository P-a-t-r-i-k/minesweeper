import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Game {
    private final Square[][] squares;
    private GameStatus gameStatus;
    private final int maximumNumberOfMines;
    private int currentNumberOfMines;

    public Game() {
        this.gameStatus = GameStatus.UNFINISHED;

        Difficulty difficulty = this.getDifficulty();
        this.maximumNumberOfMines = difficulty.getMines();
        this.currentNumberOfMines = difficulty.getMines();

        this.squares = new Square[difficulty.getHeight()][difficulty.getWidth()];
        this.createMines(this.createIndexesOfSquaresWithMines());
        this.addNumbersToSquares();
    }

    public void clickSquare(int row, int column) {
        this.squares[row][column].setClicked(true);

        if (this.squares[row][column].getSquareStatus() == SquareStatus.EMPTY) {
            this.showNearbyEmptySquares(this.squares[row][column]);
        } else if (this.squares[row][column].getSquareStatus() == SquareStatus.MINE) {
            this.gameStatus = GameStatus.LOSS;
        }
    }

    public void flagSquare(int row, int column) {
        this.squares[row][column].setFlagged(!this.squares[row][column].isFlagged());
    }

    private Difficulty getDifficulty() {
        return Difficulty.BEGINNER;
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
        int[] indexesOfSquaresWithMines = new int[this.currentNumberOfMines];
        ArrayList<Integer> usedIndexes = new ArrayList<>();
        Random random = new Random(2048);


        for (int i = 0; i < indexesOfSquaresWithMines.length; i++) {
            int randomIndex;
            do {
                randomIndex = random.nextInt(this.squares.length * this.squares[i].length);
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
            currentSquare.setClicked(true);

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
                }
            }
        }
    }
}
