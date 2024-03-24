package com.coding.hard;

import java.util.ArrayDeque;
import java.util.Deque;

public class LongestValidParentheses {

    public static int longestValidParentheses(String s) {
        int ans = 0;
        int start = 0;
        Deque<Integer> stack = new ArrayDeque<>();
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '(') {
                stack.push(i);
            } else {
                if (stack.isEmpty()) {
                    start = i + 1;
                } else {
                    stack.pop();
                    ans = Math.max(ans, stack.isEmpty() ? i - start + 1 : i - stack.peek());
                }
            }
        }
        return ans;
    }

    public static int longestValid(String s) {
        if (s == null || s.isEmpty()) {
            return 0;
        }
        int n = s.length();
        int[] dp = new int[n];
        int res = 0;
        for (int i = 1; i < n; i++) {
            if (s.charAt(i) == ')') {
                if (s.charAt(i - 1) == '(') {
                    dp[i] = 2 + (i - 2 >= 0 ? dp[i - 2] : 0);
                } else if (i - dp[i - 1] - 1 >= 0 && s.charAt(i - dp[i - 1] - 1) == '(') {
                    dp[i] = 2 + dp[i - 1] + (i - dp[i - 1] - 2 >= 0 ? dp[i - dp[i - 1] - 2] : 0);
                }
                res = Math.max(res, dp[i]);
            }
        }
        return res;
    }

    public static void main(String[] args) {
        String s = ")(()())()(";
        int len = longestValidParentheses(s);
        System.out.println(len);
        System.out.println(longestValid(s));
    }
}
