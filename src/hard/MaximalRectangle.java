package hard;

import java.util.Arrays;
import java.util.Stack;

public class MaximalRectangle {
    public static int maximalRectangle(char[][] matrix) {
        if (matrix == null || matrix.length == 0) {
            return 0;
        }
        int row = matrix.length;
        int col = matrix[0].length;
        int[][] dp = new int[row][col];
        for (int j = 0; j < col; j++) {
            dp[0][j] = matrix[0][j] - '0';
        }
        for (int i = 1; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (matrix[i][j] != '0') {
                    dp[i][j] = dp[i - 1][j] + 1;
                }
            }
        }
        if (matrix.length == 1) {
            return findRowMax(dp[0]);
        }
        int max = 0;
        for (int i = 0; i < row; i++) {
            int rowMax = findRowMax(dp[i]);
            max = Math.max(max, rowMax);
        }
        return max;
    }

    private static int findRowMax(int[] rowOriginal) {
        int[] row;
        row = Arrays.copyOfRange(rowOriginal, 0, rowOriginal.length + 1);
        int max = 0;
        int length = row.length;
        Stack<Integer> sk = new Stack<>();
        int i = 0;
        while (i < length) {
            if (sk.isEmpty() || row[i] >= row[sk.peek()]) {
                sk.push(i);
                i++;
            } else {
                int index = sk.pop();
                int area = (sk.isEmpty() ? i : (i - 1 - sk.peek())) * row[index];
                max = Math.max(max, area);
            }
        }
        return max;
    }

    public static int maximalRectangle2(char[][] matrix) {
        if (matrix.length < 1) return 0;
        int[] bars = new int[matrix[0].length];
        add(bars, matrix[0]);
        int maxArea = largestRectangleArea(bars);
        for (int y = 1; y < matrix.length; y++) {
            add(bars, matrix[y]);
            maxArea = Math.max(maxArea, largestRectangleArea(bars));
        }
        return maxArea;
    }

    private static int largestRectangleArea(int[] heights) {
        int[] stack = new int[heights.length];
        int p = -1, i = 0, max = 0;
        while (i <= heights.length) {
            if (p == -1 || (i < heights.length && heights[i] > heights[stack[p]])) {
                stack[++p] = i++;
            } else {
                max = Math.max(max, heights[stack[p]] * (i - (p > 0 ? stack[p - 1] : -1) - 1));
                p--;
            }
        }
        return max;
    }

    private static void add(int[] c1, char[] c2) {
        for (int i = 0; i < c2.length; i++) {
            if (c2[i] == '1') c1[i]++;
            else c1[i] = 0;
        }
    }

    public static void main(String[] args) {
        char[][] matrix = {
                {'1', '0', '1', '0', '0'},
                {'1', '0', '1', '1', '1'},
                {'1', '1', '1', '1', '1'},
                {'1', '0', '0', '1', '0'}};
        System.out.println(maximalRectangle2(matrix));
    }
}
