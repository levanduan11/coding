package com.coding.hackkerank.alg;

import com.coding.InputReader;
import com.coding.OutputWriter;


import java.util.Arrays;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.*;

public class BuildAString {
    public static int buildString(int a, int b, String s) {
        int N = s.length();
        int[] dp = new int[N];
        int lastL = 0;
        dp[0] = a;
        for (int k = 1; k < N; k++) {
            dp[k] = dp[k - 1] + a;
            int L = lastL + 1;
            while (L > 0) {
                String cur = s.substring(k - L + 1, k + 1);
                int idx = s.substring(0, k - L + 1).indexOf(cur);
                if (idx == -1)
                    L--;
                else {
                    dp[k] = Math.min(dp[k], dp[k - L] + b);
                    break;
                }
            }
            lastL = L;
        }
        return dp[N - 1];
    }

    public static void main(String[] args) {
        String out = Arrays.stream(InputReader.inputAsStrings("build_a_string.txt")
                        .collect(joining("")).split("-"))
                .map(s -> {
                    int a = s.charAt(0) - '0';
                    int b = s.charAt(2) - '0';
                    String string = s.substring(3);
                    return buildString(a, b, string);
                })
                .map(String::valueOf)
                .collect(joining(System.lineSeparator()));
        OutputWriter.write(out, "build_a_string.txt");
    }
}
