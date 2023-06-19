public enum Difficulty {
    BEGINNER(10, 10, 10),
    INTERMEDIATE(16, 16, 40),
    EXPERT(30, 16, 99);

    private final int width;
    private final int height;
    private final int mines;


    private Difficulty(int width, int height, int mines) {
        this.width = width;
        this.height = height;
        this.mines = mines;
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
}
