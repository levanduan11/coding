package hard;

import java.util.stream.Stream;

public class PalindromePartitioningII {
    public int minCut(String s) {
        int n = s.length();
        boolean[][] dp1 = new boolean[n][n];
        for (int i = n - 1; i >= 0; i--) {
            for (int j = i; j < n; j++) {
                dp1[i][j] = s.charAt(i) == s.charAt(j) && (j - i < 3 || dp1[i + 1][j - 1]);
            }
        }
        int[] dp2 = new int[n];
        for (int i = 0; i < n; i++) {
            if (!dp1[0][i]) {
                dp2[i] = i;
                for (int j = 1; j <= i; j++) {
                    if (dp1[j][i]) {
                        dp2[i] = Math.min(dp2[i], dp2[j - 1] + 1);
                    }
                }
            }
        }
        return dp2[n - 1];
    }

    public int minCut2(String s) {
        char[] seq = s.toCharArray();
        int n = seq.length;
        int[] dp = new int[n + 1];

        for (int i = 0; i <= n; i++) {
            dp[i] = i - 1;
        }
        for (int i = 1; i <= n; i++) {
            int lo = i - 1;
            int prev = dp[i - 1];
            dp[i] = Math.min(dp[i], prev + 1);

            while (i < n && seq[i] == seq[i - 1]) {
                i++;
                prev = Math.min(prev, dp[i - 1]);
                dp[i] = Math.min(dp[i], prev + 1);
            }
            int hi = i + 1;
            while (lo > 0 && hi <= n && seq[lo - 1] == seq[hi - 1]) {
                dp[hi] = Math.min(dp[hi], dp[lo - 1] + 1);
                lo--;
                hi++;
            }
        }
        return dp[n];
    }

    public int minCut3(String s) {
        byte[] array = s.getBytes();
        int[] dp = new int[array.length];
        for (int i = 0; i < dp.length; i++) {
            dp[i] = i;
        }
        for (int i = 0; i < array.length; i++) {
            expandFromMiddle(i, i, array, dp);
            expandFromMiddle(i, i + 1, array, dp);
        }
        return dp[array.length - 1];
    }

    private void expandFromMiddle(int left, int right, byte[] array, int[] dp) {
        while (left >= 0 && right < array.length && array[left] == array[right]) {
            if (left == 0) {
                dp[right] = 0;
                return;
            } else {
                dp[right] = Math.min(dp[right], dp[left - 1] + 1);
                left--;
                right++;
            }
        }
    }

    public static void main(String[] args) {
        var o = new PalindromePartitioningII();
        Stream.of("aab", "a", "ab")
                .map(o::minCut3)
                .forEach(System.out::println);
    }
}
