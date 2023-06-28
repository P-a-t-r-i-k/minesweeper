public class LeaderboardEntry implements Comparable<LeaderboardEntry> {
    private final String name;
    private final int score;

    public LeaderboardEntry(String name, int score) {
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return this.name;
    }

    public int getScore() {
        return this.score;
    }

    @Override
    public int compareTo(LeaderboardEntry other) {
        int result = 0;

        if (this.score < other.getScore()) {
            result = -1;
        } else if (this.score > other.getScore()) {
            result = 1;
        }

        return result;
    }
}
