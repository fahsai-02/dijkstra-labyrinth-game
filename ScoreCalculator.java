public class ScoreCalculator {
    private int optimalCost;
    private int actualCost;

    public ScoreCalculator(int optimalCost) {
        this.optimalCost = optimalCost;
        this.actualCost = 0;
    }

    public void addMoveCost(int cost) {
        actualCost += cost;
    }

    public int getActualCost() {
        return actualCost;
    }

    public int getOptimalCost() {
        return optimalCost;
    }

    public int calculateScore() {
        if (actualCost == 0) return 1000;
        double ratio = (double) optimalCost / actualCost;
        return (int) (Math.min(ratio, 1.0) * 1000);
    }

    public String getScoreDisplay() {
        return "Score: " + calculateScore() + " (" + actualCost + "/" + optimalCost + ")";
    }
}
