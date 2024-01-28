package com.coding.hard;

import com.coding.InputReader;
import com.coding.OutputWriter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ExpressionAddOperators {

    public static List<String> addOperators(String num, int target) {
        List<String> res = new ArrayList<>();
        if (num == null || num.isEmpty())
            return res;
        dfs(res, num, target, 0, "", 0, 0);
        return res;
    }

    private static void dfs(List<String> res, String num, int target, int index, String path, long value, long prevOperand) {
        if (index == num.length()) {
            if (value == target) {
                res.add(path);
            }
            return;
        }
        for (int i = index; i < num.length(); i++) {
            String currentStr = num.substring(index, i + 1);
            long currentNum = Long.parseLong(currentStr);
            if (index == 0)
                dfs(res, num, target, i + 1, currentStr, currentNum, currentNum);
            else {
                dfs(res, num, target, i + 1, path + "+" + currentStr, value + currentNum, currentNum);
                dfs(res, num, target, i + 1, path + "-" + currentStr, value - currentNum, -currentNum);
                dfs(res,
                        num,
                        target,
                        i + 1,
                        path + "*" + currentStr,
                        value - prevOperand + prevOperand * currentNum,
                        prevOperand * currentNum);
            }
            if (num.charAt(index) == '0')
                break;
        }
    }

    static List<String> res;
    static char[] nums;
    static long target;
    static int n;
    static char[] chs;

    public static List<String> addOperatorsV2(String num, int target) {
        res = new ArrayList<>();
        nums = num.toCharArray();
        ExpressionAddOperators.target = target;
        n = num.length();
        chs = new char[n + n];
        int chsPtr = 0;
        long value = 0;
        for (int i = 0; i < n; i++) {
            value = value * 10 + nums[i] - '0';
            chs[chsPtr++] = nums[i];
            helper(i + 1, chsPtr, 0, value);
            if (value == 0)
                break;
        }
        return res;
    }

    private static void helper(int numPtr, int chsPtr, long cur, long prev) {
        if (numPtr == n) {
            if (cur + prev == target) {
                res.add(new String(chs, 0, chsPtr));
            }
            return;
        }
        if (Math.abs(cur - target) > (Math.abs(prev) + 1) * Math.pow(10, n - numPtr))
            return;
        long value = 0;
        int op = chsPtr++;
        for (int i = numPtr; i < n; i++) {
            value = value * 10 + nums[i] - '0';
            chs[chsPtr++] = nums[i];
            chs[op] = '+';
            helper(i + 1, chsPtr, cur + prev, value);
            chs[op] = '-';
            helper(i + 1, chsPtr, cur + prev, -value);
            chs[op] = '*';
            helper(i + 1, chsPtr, cur, prev * value);
            if (value == 0)
                break;
        }
    }

    static List<String> ans;
    static String digits;

    static void recurse(
            int index, long previousOperand, long currentOperand, long value, List<String> ops) {
        String nums = digits;
        if (index == nums.length()) {
            if (value == target && currentOperand == 0) {
                StringBuilder sb = new StringBuilder();
                ops.subList(1, ops.size()).forEach(sb::append);
                ans.add(sb.toString());
            }
            return;
        }
        currentOperand = currentOperand * 10 + Character.getNumericValue(nums.charAt(index));
        String current_val_rep = Long.toString(currentOperand);
        if (currentOperand > 0)
            recurse(index + 1, previousOperand, currentOperand, value, ops);
        ops.add("+");
        ops.add(current_val_rep);
        recurse(index + 1, currentOperand, 0, value + currentOperand, ops);
        ops.remove(ops.size() - 1);
        ops.remove(ops.size() - 1);
        if (!ops.isEmpty()) {
            ops.add("-");
            ops.add(current_val_rep);
            recurse(index + 1, -currentOperand, 0, value - currentOperand, ops);
            ops.remove(ops.size() - 1);
            ops.remove(ops.size() - 1);
            ops.add("*");
            ops.add(current_val_rep);
            recurse(
                    index + 1,
                    currentOperand * previousOperand,
                    0,
                    value - previousOperand + (currentOperand * previousOperand),
                    ops
            );
            ops.remove(ops.size() - 1);
            ops.remove(ops.size() - 1);
        }
    }

    public static List<String> addOperatorsV3(String num, int target) {
        if (num.isEmpty())
            return new ArrayList<>();
        ExpressionAddOperators.target = target;
        digits = num;
        ans = new ArrayList<>();
        recurse(0, 0, 0, 0, new ArrayList<>());
        return ans;
    }

    public static void main(String[] args) {
        String collect = InputReader.inputAsStrings("282.txt")
                .map(String::trim)
                .map(str -> str.split(","))
                .map(inputs -> {
                    String num = inputs[0];
                    int target = Integer.parseInt(inputs[1]);
                    List<String> res = addOperatorsV3(num, target);
                    return res.toString();
                })
                .collect(Collectors.joining(System.lineSeparator()));
        OutputWriter.write(collect, "282_V3.txt");
    }
}
