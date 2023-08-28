import java.io.Serializable;

public class NumberDisplay implements Serializable {
    public static final int DISPLAY_SIZE = 3;
    private final int[] figures;

    public NumberDisplay() {
        this.figures = new int[DISPLAY_SIZE];
    }

    public void changeNumber(int newNumber) {
        boolean isBiggerThanDisplaySize = newNumber >= (int)Math.pow(10, DISPLAY_SIZE);
        if (isBiggerThanDisplaySize) {
            newNumber = 999;
        }

        for (int i = 0; i < 3; i++) {
            int remainder = newNumber % (int)Math.pow(10, i + 1);
            this.figures[this.figures.length - 1 - i] = remainder / (int)Math.pow(10, i);
        }
    }

    public int getFigure(int position) {
        if (position >= 0 && position < this.figures.length) {
            return this.figures[position];
        }

        return 0;
    }
}
