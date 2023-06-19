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
    }

    private Difficulty getDifficulty() {
        return Difficulty.BEGINNER;
    }

    private void createMines(int[] indexesOfSquaresWithMines) {
        int index = 0;
        int position = 0;

        for (int i = 0; i < this.squares.length; i++) {
            for (int j = 0; j < this.squares[i].length; j++) {

                if (index == indexesOfSquaresWithMines[position]) {
                    this.squares[i][j] = new Square(i, j, SquareStatus.MINE);
                    position++;

                    if (position == indexesOfSquaresWithMines.length) {
                        return;
                    }
                } else {
                    this.squares[i][j] = new Square(i, j, SquareStatus.EMPTY);
                }

                index++;
            }
        }
    }

    private int[] createIndexesOfSquaresWithMines() {
        int[] indexesOfSquaresWithMines = new int[this.currentNumberOfMines];
        ArrayList<Integer> usedIndexes = new ArrayList<>();
        Random random = new Random();


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
}
