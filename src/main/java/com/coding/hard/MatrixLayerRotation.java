package com.coding.hard;

import java.util.*;

public class MatrixLayerRotation {
    public static void matrixRotation(List<List<Integer>> matrix, int r) {
        int rows = matrix.size();
        int cols = matrix.get(0).size();
        int[][] tempMatrix = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            List<Integer> currRow = matrix.get(i);
            for (int j = 0; j < cols; j++) {
                tempMatrix[i][j] = currRow.get(j);
            }
        }
        int[][] finalMatrix = new int[rows][cols];

        int str = rows - 1;
        int stc = cols - 1;
        int k = 0, l = 0;
        Queue<Integer> q = new LinkedList<>();
        while (k < str && l < stc) {
            int totalBlocksInSquare = (2 * (str - k)) + (2 * (stc - k));
            int blocksToShift = r % totalBlocksInSquare;

            for (int j = stc; j >= l; j--) {
                q.add(tempMatrix[str][j]);
            }
            for (int i = (str - 1); i >= k; i--) {
                q.add(tempMatrix[i][l]);
            }
            for (int j = (l + 1); j <= stc; j++) {
                q.add(tempMatrix[k][j]);
            }
            for (int i = (k + 1); i < str; i++) {
                q.add(tempMatrix[i][stc]);
            }
            for (int i = 0; i < blocksToShift; i++) {
                q.add(q.remove());
            }
            for (int j = stc; j >= l; j--) {
                finalMatrix[str][j] = q.remove();
            }
            for (int i = (str - 1); i >= k; i--) {
                finalMatrix[i][l] = q.remove();
            }
            for (int j = (l + 1); j <= stc; j++) {
                finalMatrix[k][j] = q.remove();
            }
            for (int i = (k + 1); i < str; i++) {
                finalMatrix[i][stc] = q.remove();
            }
            str--;
            stc--;
            k++;
            l++;
        }
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.print(finalMatrix[i][j]);
                if (j != cols - 1)
                    System.out.print(" ");
            }
            System.out.println();
        }
    }

    static void test(List<List<Integer>> m) {
        int r = m.size();
        int c = m.get(0).size();
        List<List<Integer>> res = new ArrayList<>();
        for (int i = 0; i < r; i++) {
            res.add(new ArrayList<>(c));
        }
        // top
        for (int i = 0; i < c - 1; i++) {
            int g = m.get(0).get(i + 1);
            res.get(0).add(g);
        }
        int v = m.get(1).get(c - 1);
        res.get(0).add(v);
        //bottom
        for (int i = c - 1; i > 0; i--) {
            int g = m.get(r - 1).get(i - 1);
            res.get(r - 1).add(i, g);
        }
        System.out.println();
    }

    public static void main(String[] args) {
        List<List<Integer>> matrix = List.of(
                List.of(1, 2, 3, 4),
                List.of(7, 8, 9, 10),
                List.of(13, 14, 15, 16),
                List.of(19, 20, 21, 22),
                List.of(25, 26, 27, 28)
        );
        int r = 2;
        matrixRotation(matrix, r);
    }
}
