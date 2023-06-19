public class Square {
    private final int row;
    private final int column;
    private boolean isFlagged;
    private final SquareStatus squareStatus;

    public Square(int row, int column, SquareStatus squareStatus) {
        this.row = row;
        this.column = column;
        this.isFlagged = false;
        this.squareStatus = squareStatus;
    }

    public boolean isFlagged() {
        return this.isFlagged;
    }

    public SquareStatus getSquareStatus() {
        return this.squareStatus;
    }
}
