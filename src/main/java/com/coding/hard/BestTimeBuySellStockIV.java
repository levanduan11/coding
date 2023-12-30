package com.coding.hard;

public class BestTimeBuySellStockIV {
    public static int maxProfit(int k, int[] prices) {
        int n = prices.length;
        if (n <= 1)
            return 0;
        int[][][] dp = new int[n][k + 1][2];
        for (int i = 1; i <= k; i++) {
            dp[0][i][1] = -prices[0];
        }
        for (int i = 1; i < n; i++) {
            for (int j = 1; j <= k; j++) {
                dp[i][j][0] = Math.max(dp[i - 1][j][1] + prices[i], dp[i - 1][j][0]);
                dp[i][j][1] = Math.max(dp[i - 1][j - 1][0] - prices[i], dp[i - 1][j][1]);
            }
        }
        return dp[n - 1][k][0];
    }

    static int[] prices;

    static int[] solveG(int fee) {
        int bought = -prices[0];
        int sold = 0;
        int numBuys = 1;
        int numSells = 0;

        for (int i = 1; i < prices.length; i++) {
            int p = prices[i];
            int buy = sold - p;
            int sell = bought + p - fee;
            if (buy > bought) {
                bought = buy;
                numBuys = numSells + 1;
            }
            if (sell > sold) {
                sold = sell;
                numSells = numBuys;
            }
        }
        return new int[]{sold, numSells};
    }

    static int maxP(int k, int[] prices) {
        int l = 0;
        BestTimeBuySellStockIV.prices = prices;
        int r = 0;
        for (int p : prices) {
            r = Math.max(r, p);
        }
        int bestFee = 0;
        while (l <= r) {
            int mid = l + (r - l) / 2;
            int[] sol = solveG(mid);
            if (sol[1] > k)
                l = mid + 1;
            else {
                bestFee = mid;
                r = mid - 1;
            }
        }
        int[] sol = solveG(bestFee);
        return sol[0] + bestFee * k;
    }

    public static void main(String[] args) {
        int[] prices = {3, 2, 6, 5, 0, 3};
        int k = 2;
        int res = maxProfit(k, prices);
        int res2 = maxP(k, prices);
        System.out.println(res);
        System.out.println(res2);
    }
}
