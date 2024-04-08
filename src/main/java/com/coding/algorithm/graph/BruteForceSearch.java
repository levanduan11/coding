package com.coding.algorithm.graph;

public class BruteForceSearch {
    public static int search(String text, String pattern) {
        int n = text.length();
        int m = pattern.length();
        outer:
        for (int i = 0; i <= n - m; i++) {
            for (int j = 0; j < m; j++) {
                if (text.charAt(i + j) != pattern.charAt(j)) {
                    continue outer;
                }
            }
            return i;
        }
        return -1;
    }

    public static void main(String[] args) {
        String text = "AABAACAADAABAAABAA";
        String pattern = "DAAB";
        System.out.println(search(text, pattern));
    }
}
