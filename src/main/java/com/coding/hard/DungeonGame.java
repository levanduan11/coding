package com.coding.hard;

import java.util.Arrays;

public class DungeonGame {
    public static int calculateMinimumHP(int[][] dungeon) {
        int m = dungeon.length, n = dungeon[0].length;
        int[][] dp = new int[m + 1][n + 1];
        for (int[] e : dp) {
            Arrays.fill(e, 1 << 30);
        }
        dp[m][n - 1] = dp[m - 1][n] = 1;
        for (int i = m - 1; i >= 0; i--) {
            for (int j = n - 1; j >= 0; j--) {
                dp[i][j] = Math.max(1, Math.min(dp[i + 1][j], dp[i][j + 1]) - dungeon[i][j]);
            }
        }
        return dp[0][0];
    }

    static int dfs(int[][] dungeon, int[][] dp, int row, int col) {
        if (row == dp.length || col == dp[0].length)
            return Integer.MAX_VALUE;

        if (dp[row][col] != -1) return dp[row][col];

        int a = dfs(dungeon, dp, row, col + 1);
        if (a != Integer.MAX_VALUE)
            a = a - dungeon[row][col];
        a = a > 0 ? a : 1;
        int b = dfs(dungeon, dp, row + 1, col);
        if (b != Integer.MAX_VALUE)
            b = b - dungeon[row][col];
        b = b > 0 ? b : 1;
        dp[row][col] = Math.min(a, b);
        return dp[row][col];
    }

    static int calculate(int[][] dungeon) {
        int m = dungeon.length, n = dungeon[0].length;
        int[][] dp = new int[m][n];
        for (int[] d : dp) {
            Arrays.fill(d, -1);
        }
        dp[m - 1][n - 1] = Math.max(1, 1 - dungeon[m - 1][n - 1]);
        int ans = dfs(dungeon, dp, 0, 0);
        return ans;
    }


    public static void main(String[] args) {
        int[][] dungeon = {{-2, -3, 3}, {-5, -10, 1}, {10, 30, -5}};
        int res = calculateMinimumHP(dungeon);
        int res2 = calculate(dungeon);
        System.out.println(res);
        System.out.println(res2);
    }
}
