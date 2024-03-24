package com.coding.hard;

import java.util.Arrays;

public class NQueensII {
    int n;
    int ans;

    public int totalNQueens(int n) {
        this.n = n;
        int[][] grid = new int[n][n];
        return solve(grid, 0);
    }

    boolean isValid(int[][] grid, int row, int col) {
        int i, j;
        for (i = 0; i < col; i++) {
            if (grid[row][i] == 1) return false;
        }
        for (i = row, j = col; i >= 0 && j >= 0; i--, j--) {
            if (grid[i][j] == 1) return false;
        }
        for (i = row, j = col; i < n && j >= 0; i++, j--) {
            if (grid[i][j] == 1) return false;
        }
        return true;
    }

    int solve(int[][] grid, int col) {
        if (col == n) {
            return 1;
        }
        int v = 0;
        for (int i = 0; i < n; i++) {
            if (isValid(grid, i, col)) {
                grid[i][col] = 1;
                v += (solve(grid, col + 1));
                grid[i][col] = 0;
            }
        }
        return v;
    }

    private void dfs(int n, int i, boolean[] cols, boolean[] diag1, boolean[] diag2) {
        if (i == n) {
            ++ans;
            return;
        }
        for (int j = 0; j < cols.length; j++) {
            int k = i + j, l = j - i + n - 1;
            if (cols[j] || diag1[k] || diag2[l]) continue;
            cols[j] = diag1[k] = diag2[l] = true;
            dfs(n, i + 1, cols, diag1, diag2);
            cols[j] = diag1[k] = diag2[l] = false;
        }
    }

    public int total(int n) {
        dfs(n, 0, new boolean[n], new boolean[2 * n - 1], new boolean[2 * n - 1]);
        return ans;
    }

    public static void main(String[] args) {
        var queen = new NQueensII();
        System.out.println(queen.totalNQueens(4));
        System.out.println(queen.total(4));
    }
}
