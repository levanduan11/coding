package com.coding.algorithm.graph;

public class TravelingSaleManDP {
    static int n = 4;
    static int MAX = 1_000_000;
    static int[][] dist = {
            {0, 0, 0, 0, 0},
            {0, 0, 10, 15, 20},
            {0, 10, 0, 25, 25},
            {0, 15, 25, 0, 30},
            {0, 20, 25, 30, 0},
    };
    static int[][] memo = new int[n + 1][1 << (n + 1)];

    static int fun(int i, int mask) {
        String s = Integer.toBinaryString(mask);
        if (mask == ((1 << i) | 3))
            return dist[1][i];

        if (memo[i][mask] != 0)
            return memo[i][mask];

        int res = MAX;

        for (int j = 1; j <= n; j++) {
            if ((mask & (1 << j)) != 0 && j != i && j != 1) {
                String v = Integer.toBinaryString(mask & (~(1 << i)));
                int prev = fun(j, mask & (~(1 << i)));
                res = Math.min(res, prev + dist[j][i]);
            }
        }
        return memo[i][mask] = res;
    }

    public static void main(String[] args) {
        int ans = MAX;
        for (int i = 1; i <= n; i++) {
            int v = fun(i, (1 << (n + 1)) - 1);
            ans = Math.min(ans, v + dist[i][1]);
        }

        System.out.println("The cost of most efficient tour = " + ans);
    }
}
