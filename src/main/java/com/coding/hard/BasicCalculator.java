package com.coding.hard;

import com.coding.InputReader;

import java.util.Arrays;
import java.util.Stack;

public class BasicCalculator {

    private static int charToNumber(char ch) {
        return ch - '0';
    }

    private static char numberToChar(int number) {
        return (char) (number + '0');
    }

    public static int calculate2(String s) {
        if (s.isEmpty())
            return 0;
        String ss = clean(s);
        boolean numberValid = isNumber(ss);
        if (numberValid) {
            return Integer.parseInt(ss);
        }
        String postfix = infixToPostfix(ss);
        Stack<Character> stack = new Stack<>();
        for (char c : postfix.toCharArray()) {
            if (c >= '0' && c <= '9') {
                stack.push(c);
            } else if (!stack.isEmpty() && stack.size() > 1) {
                char v2 = stack.pop();
                char v = stack.pop();
                int r = switch (c) {
                    case '+' -> charToNumber(v) + charToNumber(v2);
                    case '-' -> charToNumber(v) - charToNumber(v2);
                    default -> throw new IllegalArgumentException();
                };
                stack.push(numberToChar(r));
            }
        }
        return stack.isEmpty() ? 0 : charToNumber(stack.peek());
    }

    private static String clean(String s) {
        StringBuilder sb = new StringBuilder();
        for (char c : s.toCharArray()) {
            if (c == ' ')
                continue;
            sb.append(c);
        }
        return sb.toString();
    }

    private static boolean isNumber(String s) {
        if (s == null || s.isEmpty())
            return false;
        for (char c : s.toCharArray()) {
            if (c < '0' || c > '9')
                return false;
        }
        return true;
    }

    private static int precedence(char x) {
        if (x == '^') {
            return 2;
        } else if (x == '*' || x == '/') {
            return 1;
        } else if (x == '+' || x == '-') {
            return 0;
        }
        return -1;
    }

    private static String infixToPostfix(String str) {
        Stack<Character> stack = new Stack<>();
        StringBuilder sb = new StringBuilder();
        for (int i = 0, n = str.length(); i < n; i++) {
            char x = str.charAt(i);
            if (x == ' ')
                continue;
            if (x >= '0' && x <= '9') {
                sb.append(x);
            } else if (x == '(') {
                stack.push(x);
            } else if (x == ')') {
                while (!stack.isEmpty() && stack.peek() != '(') {
                    sb.append(stack.pop());
                }
                if (!stack.isEmpty()) {
                    stack.pop();
                }
            } else {
                while (!stack.isEmpty() && precedence(stack.peek()) >= precedence(x)) {
                    sb.append(stack.pop());
                }
                stack.push(x);
            }
        }
        while (!stack.isEmpty()) {
            sb.append(stack.pop());
        }
        return sb.toString();
    }

    public static int calculate(String s) {
        int res = 0, num = 0, sign = 1, n = s.length();
        Stack<Integer> sk = new Stack<>();
        for (int i = 0; i < n; i++) {
            char c = s.charAt(i);
            if (c >= '0') {
                num = 10 * num + (c - '0');
            } else if (c == '+' || c == '-') {
                res += sign * num;
                num = 0;
                sign = (c == '+') ? 1 : -1;
            } else if (c == '(') {
                sk.push(res);
                sk.push(sign);
                res = 0;
                sign = 1;
                num = 0;
            } else if (c == ')') {
                res += sign * num;
                num = 0;
                res *= sk.peek();
                sk.pop();
                res += sk.peek();
                sk.pop();
            }
        }
        res += sign * num;
        return res;
    }

    private static int INDEX;

    private static int calculate03(String s) {
        INDEX = 0;
        return calc(s.toCharArray());
    }

    private static int calc(char[] array) {
        int sum = 0;
        int nextSign = 1;
        while (INDEX < array.length) {
            switch (array[INDEX]) {
                case ' ' -> {
                }
                case '+' -> nextSign = 1;
                case '-' -> nextSign = -1;
                case '(' -> {
                    INDEX++;
                    sum += (nextSign * calc(array));
                }
                case ')' -> {
                    return sum;
                }
                default -> {
                    int number = array[INDEX] - '0';
                    while (INDEX + 1 < array.length && array[INDEX + 1] >= '0') {
                        INDEX++;
                        number = number * 10 + (array[INDEX] - '0');
                    }
                    sum += (nextSign * number);
                }
            }
            INDEX++;
        }
        return sum;
    }

    public static void main(String[] args) {

    }
}
