package hard;

class Solution1 {
    private final int[][] moves = {{2, 1}, {-2, 1}, {1, 2}, {-1, 2}, {2, -1}, {-2, -1}, {1, -2}, {-1, -2}};

    public double knightProbability(int n, int k, int row, int column) {
        double[][][] cache = new double[n / 2 + 1][n / 2 + 1][k + 1];
        return solver(n, k, row, column, cache);
    }

    private double solver(int n, int k, int row, int column, double[][][] memo) {
        if (row < 0 || row >= n || column < 0 || column >= n) return 0.0;
        if (k == 0) return 1.0;

        row = Math.min(row, n - 1 - row);
        column = Math.min(column, n - 1 - column);

        if (row < column) return solver(n, k, column, row, memo);
        if (memo[row][column][k] != 0.0) return memo[row][column][k];

        double probability = 0.0;
        for (int[] move : moves) {
            probability += solver(n, k - 1, row + move[0], column + move[1], memo) / 8;
        }
        memo[row][column][k] = probability;
        return probability;
    }

    public static void main(String[] args) {
        int n = 3, k = 2, row = 0, column = 0;
        var o = new Solution1();
        System.out.println(o.knightProbability(n, k, row, column));
    }
}

public class KnightProbabilityChessboard {
    public double knightProbability(int n, int k, int row, int column) {
        double[][][] dp = new double[k + 1][n][n];
        int[] dirs = {-2, -1, 2, 1, -2, 1, 2, -1, -2};
        for (int l = 0; l <= k; l++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (l == 0) {
                        dp[l][i][j] = 1;
                    } else {
                        for (int d = 0; d < 8; d++) {
                            int x = i + dirs[d], y = j + dirs[d + 1];
                            if (x >= 0 && x < n && y >= 0 && y < n) {
                                dp[l][i][j] += dp[l - 1][x][y] / 8;
                            }
                        }
                    }
                }
            }
        }
        return dp[k][row][column];
    }

    public static void main(String[] args) {
        int n = 3, k = 2, row = 0, column = 0;
        var o = new KnightProbabilityChessboard();
        System.out.println(o.knightProbability(n, k, row, column));
    }
}
