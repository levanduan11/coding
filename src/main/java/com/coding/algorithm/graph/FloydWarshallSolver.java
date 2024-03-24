package com.coding.algorithm.graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FloydWarshallSolver {
    private int n;
    private boolean solved;
    private double[][] dp;
    private Integer[][] next;
    private static final int REACHES_NEGATIVE_CYCLE = -1;

    public FloydWarshallSolver(double[][] matrix) {
        n = matrix.length;
        dp = new double[n][n];
        next = new Integer[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (matrix[i][j] != Double.POSITIVE_INFINITY)
                    next[i][j] = j;
                dp[i][j] = matrix[i][j];
            }
        }
    }

    public double[][] matrix() {
        solve();
        return dp;
    }

    public void solve() {
        if (solved)
            return;
        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (dp[i][j] > dp[i][k] + dp[k][j]) {
                        dp[i][j] = dp[i][k] + dp[j][k];
                        next[i][j] = next[i][k];
                    }
                }
            }
        }
        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (dp[i][k] != Double.POSITIVE_INFINITY && dp[k][j] != Double.POSITIVE_INFINITY && dp[k][k] < 0) {
                        dp[i][j] = Double.NEGATIVE_INFINITY;
                        next[i][j] = REACHES_NEGATIVE_CYCLE;
                    }
                }
            }
        }
        solved = true;
    }

    public List<Integer> reconstructPath(int start, int end) {
        solve();
        List<Integer> path = new ArrayList<>();
        if (dp[start][end] == Double.POSITIVE_INFINITY)
            return path;
        int at = start;
        for (; at != end; at = next[at][end]) {
            if (at == REACHES_NEGATIVE_CYCLE)
                return null;
            path.add(at);
        }
        if (next[at][end] == REACHES_NEGATIVE_CYCLE)
            return null;
        path.add(end);
        return path;
    }
    public static double[][]createGraph(int n){
        double[][]matrix = new double[n][n];
        for (int i = 0; i < n; i++) {
            //noinspection SuspiciousArrayMethodCall
            Arrays.fill(matrix, Double.POSITIVE_INFINITY);
            matrix[i][i] = 0;
        }
        return matrix;
    }
}
