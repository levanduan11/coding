package com.coding.hard;

import com.coding.InputReader;
import com.coding.OutputWriter;

import java.util.*;
import java.util.stream.Collectors;

public class LongestIncreasingPathMatrix {
    static int[][] dirs = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
    static int m, n;
    static int[][] matrix;
    static int[][] paths;
    static int maxPath;

    public static int longestIncreasingPathV4(int[][] matrix) {
        int m = matrix.length, n = matrix[0].length;
        int[][] indegree = new int[m][n];
        int[][] dirs = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                for (int[] dir : dirs) {
                    int x = i + dir[0];
                    int y = j + dir[1];
                    if (x >= 0 && x < m && y >= 0 && y < n && matrix[x][y] < matrix[i][j])
                        indegree[i][j]++;
                }
            }
        }
        Deque<int[]> queue = new ArrayDeque<>();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (indegree[i][j] == 0)
                    queue.offer(new int[]{i, j});
            }
        }
        int maxPath = 0;
        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                int[] cell = queue.poll();
                if (cell != null) {
                    int row = cell[0], col = cell[1];
                    for (int[] dir : dirs) {
                        int x = row +dir[0];
                        int y = col +dir[1];
                        if (x >= 0 && x < m && y >= 0 && y < n && matrix[x][y] > matrix[row][col]) {
                            indegree[x][y]--;
                            if (indegree[x][y] == 0)
                                queue.offer(new int[]{x, y});
                        }
                    }
                }
            }
            maxPath++;
        }
        return maxPath;
    }

    public static int longestIncreasingPathV3(int[][] matrix) {
        LongestIncreasingPathMatrix.matrix = matrix;
        m = matrix.length;
        n = matrix[0].length;
        paths = new int[m][n];
        maxPath = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (paths[i][j] == 0) {
                    dfs(i, j);
                }
            }
        }
        return maxPath;
    }

    private static int dfs(int i, int j) {
        if (paths[i][j] != 0)
            return paths[i][j];
        int up = 0;
        int down = 0;
        int left = 0;
        int right = 0;
        if (i > 0 && matrix[i - 1][j] > matrix[i][j])
            up = dfs(i - 1, j);
        if (i < m - 1 && matrix[i + 1][j] > matrix[i][j])
            down = dfs(i + 1, j);
        if (j > 0 && matrix[i][j - 1] > matrix[i][j])
            left = dfs(i, j - 1);
        if (j < n - 1 && matrix[i][j + 1] > matrix[i][j])
            right = dfs(i, j + 1);
        paths[i][j] = 1 + Math.max(left, Math.max(right, Math.max(up, down)));
        maxPath = Math.max(maxPath, paths[i][j]);
        return paths[i][j];
    }

    public static int longestIncreasingPath(int[][] matrix) {
        int res = 0;
        m = matrix.length;
        n = matrix[0].length;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                int dfsRes = dfs(matrix, i, j, new HashMap<>());
                res = Math.max(res, dfsRes);
            }
        }
        return res;
    }

    private static int dfs(int[][] matrix, int i, int j, Map<String, Integer> memo) {
        if (memo.containsKey(i + ":" + j))
            return memo.get(i + ":" + j);
        int res = 1;
        for (int[] dir : dirs) {
            int x = i + dir[0];
            int y = j + dir[1];
            if (x >= 0 && x < m && y >= 0 && y < n && matrix[x][y] > matrix[i][j]) {
                int dfsRes = 1 + dfs(matrix, x, y, memo);
                res = Math.max(res, dfsRes);
            }
        }
        memo.put(i + ":" + j, res);
        return res;
    }

    public static int longestIncreasingPathV2(int[][] matrix) {
        int res = 0;
        int m = matrix.length;
        int n = matrix[0].length;
        int[][] dp = new int[m][n];
        for (int i = 0; i < m; i++) {
            Arrays.fill(dp[i], -1);
        }
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                res = Math.max(res, dfs(matrix, i, j, dp));
            }
        }
        return res;
    }

    private static int dfs(int[][] matrix, int i, int j, int[][] dp) {
        if (dp[i][j] != -1) {
            return dp[i][j];
        }
        int m = matrix.length;
        int n = matrix[0].length;
        int[] dx = {0, 0, 1, -1};
        int[] dy = {1, -1, 0, 0};
        int res = 1;
        for (int k = 0; k < 4; k++) {
            int x = i + dx[k];
            int y = j + dy[k];
            if (x >= 0 && x < m && y >= 0 && y < n && matrix[x][y] > matrix[i][j]) {
                res = Math.max(res, 1 + dfs(matrix, x, y, dp));
            }
        }
        dp[i][j] = res;
        return res;
    }

    public static void main(String[] args) {
        String out = InputReader.inputAsStrings("329.txt")
                .map(line -> {
                    String[] arr = line.split("],\\[");
                    int[][] grid = new int[arr.length][];
                    int r = 0;
                    for (String s : arr) {
                        String[] chars = s.split(",");
                        int[] row = new int[chars.length];
                        for (int i = 0; i < row.length; i++) {
                            String c = chars[i];
                            int j = 0;
                            for (char c1 : c.toCharArray()) {
                                if (c1 >= '0' && c1 <= '9') {
                                    j = j * 10 + (c1 - '0');
                                }
                            }
                            row[i] = j;
                        }
                        grid[r++] = row;
                    }
                    return longestIncreasingPathV4(grid);
                })
                .map(String::valueOf)
                .collect(Collectors.joining(System.lineSeparator()));
        OutputWriter.write(out, "329.txt");
    }
}
