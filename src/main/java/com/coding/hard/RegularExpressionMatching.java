package com.coding.hard;

import java.util.regex.Pattern;

public class RegularExpressionMatching {
    public boolean isMatch(String s, String p) {
        if (s == null || p == null) {
            return false;
        }
        if (p.length() == 0)
            return s.length() == 0;

        if (p.length() == 1)
            return (s.length() == 1 && (s.charAt(0) == p.charAt(0) || p.charAt(0) == '.' || p.charAt(0) == '*'));

        if (p.charAt(1) != '*') {
            if (s.length() == 0) {
                return false;
            } else {
                return (s.charAt(0) == p.charAt(0) || p.charAt(0) == '.') && isMatch(s.substring(1), p.substring(1));
            }
        }
        int i = 0;
        while (i < s.length() && (s.charAt(i) == p.charAt(0) || p.charAt(0) == '.')) {
            if (isMatch(s.substring(i + 1), p.substring(2))) {
                return true;
            }
            i++;
        }
        return isMatch(s, p.substring(2));
    }


    public static boolean isMatch1(String s, String p) {
        int m = s.length();
        int n = p.length();
        boolean[] dp = new boolean[n + 1];
        dp[0] = true;

        // case a*b*c
        for (int i = 1; i <= n; i++) {
            if (p.charAt(i - 1) == '*') {
                dp[i] = dp[i - 2];
            }
        }
        for (int i = 1; i <= m; i++) {
            boolean prev = dp[0];
            dp[0] = false;
            for (int j = 1; j <= n; j++) {
                boolean temp = dp[j];
                char sc = s.charAt(i - 1);
                char pc = p.charAt(j - 1);

                if (sc == pc || pc == '.') {
                    dp[j] = prev;
                } else if (pc == '*') {
                    char preChar = p.charAt(j - 2);
                    if (preChar != sc && preChar != '.') {
                        dp[j] = dp[j - 2];
                    } else {
                        boolean b = dp[j] ||( dp[j - 1] == dp[j - 2]);
                        dp[j] = b;
                    }
                } else {
                    dp[j] = false;
                }
                prev = temp;
            }
        }
        return dp[n];
    }

    public static void main(String[] args) {
        var o = new RegularExpressionMatching();
        String s = "cccbbbaaa";
        String p = "c*b*a*";
        System.out.println(isMatch1(s, p));
        Pattern pattern=Pattern.compile(p);
        System.out.println( pattern.matcher(s).matches());
    }
}
