public enum Difficulty {
    BEGINNER(10, 10, 10, 370, 130),
    INTERMEDIATE(16, 16, 40, 265, 40),
    EXPERT(30, 16, 99, 40, 40);

    private final int width;
    private final int height;
    private final int mines;
    private final int difficultyBonus;
    private final int indentationX;
    private final int indentationY;


    Difficulty(int width, int height, int mines, int indentationX, int indentationY) {
        this.width = width;
        this.height = height;
        this.mines = mines;
        this.difficultyBonus = mines * 100;
        this.indentationX = indentationX;
        this.indentationY = indentationY;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public int getMines() {
        return this.mines;
    }

    public int getDifficultyBonus() {
        return this.difficultyBonus;
    }

    public int getIndentationX() {
        return this.indentationX;
    }

    public int getIndentationY() {
        return this.indentationY;
    }
}
