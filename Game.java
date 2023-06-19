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
        this.createMines(this.createSquaresIndexesWithMines());
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
                }

                this.squares[i][j] = new Square(i, j, squareStatus);
                index++;
            }
        }
    }

    private int[] createSquaresIndexesWithMines() {
        int[] indexesOfSquaresWithMines = new int[this.currentNumberOfMines];
        Random random = new Random();

        for (int i = 0; i < indexesOfSquaresWithMines.length; i++) {
            indexesOfSquaresWithMines[i] = random.nextInt(this.squares.length * this.squares[i].length);
        }

        Arrays.sort(indexesOfSquaresWithMines);

        return indexesOfSquaresWithMines;
    }
}
