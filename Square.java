public class Square {
    private final int row;
    private final int column;
    private boolean clicked;
    private boolean isFlagged;
    private int nearbyMines;
    private SquareStatus squareStatus;

    public Square(int row, int column, SquareStatus squareStatus) {
        this.row = row;
        this.column = column;
        this.clicked = false;
        this.isFlagged = false;
        this.squareStatus = squareStatus;
    }

    public int getRow() {
        return this.row;
    }

    public int getColumn() {
        return this.column;
    }

    public boolean isClicked() {
        return this.clicked;
    }

    public void setClicked(boolean clicked) {
        this.clicked = clicked;
    }

    public boolean isFlagged() {
        return this.isFlagged;
    }

    public void setFlagged(boolean flagged) {
        this.isFlagged = flagged;
    }

    public int getNearbyMines() {
        return this.nearbyMines;
    }

    public void setNearbyMines(int nearbyMines) {
        this.nearbyMines = nearbyMines;
    }

    public SquareStatus getSquareStatus() {
        return this.squareStatus;
    }

    public void setSquareStatus(SquareStatus squareStatus) {
        this.squareStatus = squareStatus;
    }
}
