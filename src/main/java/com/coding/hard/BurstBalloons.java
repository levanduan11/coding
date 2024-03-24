package com.coding.hard;

import com.coding.InputReader;
import com.coding.OutputWriter;

import java.util.*;
import java.util.stream.Collectors;

public class BurstBalloons {
    public static int maxCoins(int[] nums) {
        int[] vals = new int[nums.length + 2];
        vals[0] = 1;
        vals[vals.length - 1] = 1;
        System.arraycopy(nums, 0, vals, 1, nums.length);
        int n = vals.length;
        int[][] dp = new int[n][n];
        for (int l = 2; l < n; l++) {
            for (int i = 0; i + l < n; i++) {
                int j = i + l;
                for (int k = i + 1; k < j; k++) {
                    dp[i][j] = Math.max(dp[i][j], dp[i][k] + dp[k][j] + vals[i] * vals[k] * vals[j]);
                }
            }
        }
        return dp[0][n - 1];
    }

    public static int maxCoinsV2(int[] nums) {
        int n = nums.length;
        int[] newnums = new int[n + 2];
        newnums[0] = 1;
        newnums[n + 1] = 1;
        System.arraycopy(nums, 0, newnums, 1, n);
        int[][] dp = new int[n + 2][n + 2];
        for (int len = 2; len < n + 2; len++) {
            for (int left = 0; left + len < n + 2; left++) {
                int right = left + len;
                int ij = newnums[left] * newnums[right];
                int ans = 0;
                for (int i = left + 1; i < right; i++) {
                    int v = ij * newnums[i] + dp[left][i] + dp[i][right];
                    ans = Math.max(ans, v);
                }
                dp[left][right] = ans;
            }
        }
        return dp[0][n + 1];
    }

    public static int maxCoinsV3(int[] nums) {
        int[][] memo = new int[nums.length + 2][nums.length + 2];
        for (int[] ints : memo) {
            Arrays.fill(ints, -1);
        }
        int[] arr = new int[nums.length + 2];
        arr[0] = 1;
        arr[arr.length - 1] = 1;
        System.arraycopy(nums, 0, arr, 1, arr.length - 1 - 1);
        return maxCoinsV3(1, nums.length, arr, memo);
    }

    public static int maxCoinsV3(int i, int j, int[] arr, int[][] memo) {
        if (i > j) {
            return 0;
        }
        if (memo[i][j] != -1)
            return memo[i][j];
        int max = Integer.MIN_VALUE;
        for (int k = i; k <= j; k++) {
            int l = maxCoinsV3(i, k - 1, arr, memo);
            int r = maxCoinsV3(k + 1, j, arr, memo);
            int count = arr[i - 1] * arr[k] * arr[j + 1] + l + r;
            max = Math.max(count, max);
        }
        memo[i][j] = max;
        return max;
    }

    public static void main(String[] args) {
        String out = InputReader.inputAsStrings("321.txt")
                .filter(Objects::nonNull)
                .map(s -> s.split(","))
                .map(strings -> {
                    int[] ints = new int[strings.length];
                    for (int i = 0; i < strings.length; i++) {
                        ints[i] = Integer.parseInt(strings[i]);
                    }
                    return ints;
                })
                .map(BurstBalloons::maxCoinsV3)
                .map(integer -> Integer.toString(integer))
                .collect(Collectors.joining(System.lineSeparator()));
        OutputWriter.write(out, "321_v3.txt");
    }
}
