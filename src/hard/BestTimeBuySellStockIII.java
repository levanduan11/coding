package hard;

public class BestTimeBuySellStockIII {
    public static int maxProfit(int[] prices) {
        if (prices == null || prices.length == 0)
            return 0;
        int[] leftMax = new int[prices.length];
        int tmpMin = prices[0];
        for (int i = 1; i < prices.length; i++) {
            leftMax[i] = Math.max(leftMax[i - 1], prices[i] - tmpMin);
            tmpMin = Math.min(tmpMin, prices[i]);
        }
        int[] rightMax = new int[prices.length];
        int tmpMax = prices[prices.length - 1];
        for (int i = prices.length - 2; i >= 0; i--) {
            rightMax[i] = Math.max(rightMax[i + 1], tmpMax - prices[i]);
            tmpMax = Math.max(tmpMax, prices[i]);
        }
        int max = 0;
        for (int i = 1; i < prices.length; i++) {
            max = Math.max(max, leftMax[i] + rightMax[i]);
        }
        return max;
    }

    public static int maxProfit2(int[] prices) {
        int b1 = Integer.MIN_VALUE;
        int p1 = 0;
        int b2 = Integer.MIN_VALUE;
        int p2 = 0;
        for (int i = 0; i < prices.length; i++) {
            b1 = Math.max(b1, -prices[i]);
            p1 = Math.max(p1, prices[i] + b1);
            b2 = Math.max(b2, p1 - prices[i]);
            p2 = Math.max(p2, b2 + prices[i]);
        }
        return p2;
    }

    public static void main(String[] args) {
        int[] prices = {1, 2, 3, 4, 5};
        System.out.println(maxProfit(prices));
        System.out.println(maxProfit2(prices));
    }
}
