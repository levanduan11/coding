package com.coding.hard;

import com.coding.InputReader;
import com.coding.OutputWriter;

import java.util.*;
import java.util.stream.Collectors;

public class RemoveInvalidParentheses {
    public static List<String> removeInvalidParenthesesV3(String s) {
        List<String> ans = new ArrayList<>();
        remove(s, ans, 0, 0, new char[]{'(', ')'});
        return ans;
    }

    public static void remove(String s, List<String> ans, int last_i, int last_j, char[] par) {
        for (int stack = 0, i = last_i; i < s.length(); i++) {
            if (s.charAt(i) == par[0])
                stack++;
            if (s.charAt(i) == par[1])
                stack--;
            if (stack >= 0)
                continue;
            for (int j = last_j; j <= i; j++) {
                if (s.charAt(j) == par[1] && (j == last_j || s.charAt(j - 1) != par[1]))
                    remove(s.substring(0, j) + s.substring(j + 1), ans, i, j, par);
            }
            return;
        }
        String reversed = new StringBuilder(s).reverse().toString();
        if (par[0] == '(')
            remove(reversed, ans, 0, 0, new char[]{')', '('});
        else
            ans.add(reversed);
    }

    public static List<String> removeInvalidParenthesesV2(String s) {
        int open = 0, close = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '(')
                open++;
            else if (s.charAt(i) == ')') {
                if (open > 0)
                    open--;
                else
                    close++;
            }
        }
        List<String> ans = new ArrayList<>();
        dfs(s, 0, new char[s.length() - open - close], 0, open, close, 0, ans);
        return ans;
    }

    private static void dfs(String s, int index, char[] acc, int aIndex, int open, int close, int balance, List<String> result) {
        if (balance < 0)
            return;
        if (index == s.length()) {
            if (open == 0 && close == 0) {
                result.add(String.valueOf(acc));
                return;
            }
        }
        char c = s.charAt(index);
        if (c == '(' && open > 0 && check(acc, aIndex, '(')) {
            dfs(s, index + 1, acc, aIndex, open - 1, close, balance, result);
        } else if (c == ')' && close > 0 && check(acc, aIndex, ')')) {
            dfs(s, index + 1, acc, aIndex, open, close - 1, balance, result);
        }
        if (aIndex != acc.length) {
            acc[aIndex] = c;
            int nextBalance = balance;
            if (c == '(')
                nextBalance++;
            else if (c == ')')
                nextBalance--;
            dfs(s, index + 1, acc, aIndex + 1, open, close, nextBalance, result);
        }
    }

    private static boolean check(char[] arr, int idx, char brace) {
        return (idx == 0 || arr[idx - 1] != brace);
    }

    public static List<String> removeInvalidParentheses(String s) {
        List<String> result = new ArrayList<>();
        Set<String> visited = new HashSet<>();
        Queue<String> queue = new LinkedList<>();
        boolean found = false;
        queue.offer(s);
        visited.add(s);
        while (!queue.isEmpty()) {
            String current = queue.poll();
            if (isValid(current)) {
                result.add(current);
                found = true;
            }
            if (found)
                continue;
            for (int i = 0; i < current.length(); i++) {
                if (current.charAt(i) == '(' || current.charAt(i) == ')') {
                    String newStr = current.substring(0, i) + current.substring(i + 1);
                    if (!visited.contains(newStr)) {
                        visited.add(newStr);
                        queue.offer(newStr);
                    }
                }
            }
        }
        return result;
    }

    private static boolean isValid(String s) {
        int count = 0;
        for (char c : s.toCharArray()) {
            if (c == '(')
                count++;
            else if (c == ')') {
                count--;
                if (count < 0)
                    return false;
            }
        }
        return count == 0;
    }


    public static void main(String[] args) {
        String output = InputReader.inputAsStrings("301.txt")
                .map(RemoveInvalidParentheses::removeInvalidParenthesesV3)
                .map(List::toString)
                .collect(Collectors.joining(System.lineSeparator()));
        OutputWriter.write(output, "301_V3.txt");
    }
}
