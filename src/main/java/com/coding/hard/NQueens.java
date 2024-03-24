package com.coding.hard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NQueens {
    int n;
    List<List<String>> ans;

    public List<List<String>> solveNQueens(int n) {
        this.n = n;
        this.ans = new ArrayList<>();
        int[][] grid = new int[n][n];
        solve(grid, 0);
        return ans;
    }

    public boolean isSafe(int[][] grid, int row, int col) {
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

    public boolean solve(int[][] grid, int col) {
        if (col == n) {
            List<String> strings = new ArrayList<>();
            for (int[] g : grid) {
                StringBuilder sb = new StringBuilder();
                for (int i : g) {
                    if (i == 0) sb.append(".");
                    else sb.append("Q");
                }
                strings.add(sb.toString());
            }
            ans.add(strings);
            return false;
        }
        for (int i = 0; i < n; i++) {
            if (isSafe(grid, i, col)) {
                grid[i][col] = 1;
                if (solve(grid, col + 1)) return true;
                grid[i][col] = 0;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        var queen = new NQueens();
        queen.solveNQueens(4);
        System.out.println("done");

    }
}

class NQueens1 {

    public static void main(String[] args) {
        int n = 4; // Change 'n' to the desired board size
        solveNQueens(n);
    }

    public static void solveNQueens(int n) {
        int[] queens = new int[n];
        placeQueens(queens, 0, n);
    }

    public static void placeQueens(int[] queens, int row, int n) {
        if (row == n) {
            printQueens(queens);
        } else {
            for (int col = 0; col < n; col++) {
                if (isValidPlacement(queens, row, col)) {
                    queens[row] = col;
                    placeQueens(queens, row + 1, n);
                }
            }
        }
    }

    public static boolean isValidPlacement(int[] queens, int row, int col) {
        for (int i = 0; i < row; i++) {
            if (queens[i] == col || Math.abs(queens[i] - col) == Math.abs(i - row)) {
                return false;
            }
        }
        return true;
    }

    public static void printQueens(int[] queens) {
        int n = queens.length;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (queens[i] == j) {
                    System.out.print("Q ");
                } else {
                    System.out.print(". ");
                }
            }
            System.out.println();
        }
        System.out.println();
    }
}
