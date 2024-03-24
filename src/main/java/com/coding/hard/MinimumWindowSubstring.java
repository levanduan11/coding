package com.coding.hard;

import java.util.HashMap;
import java.util.Map;

public class MinimumWindowSubstring {
    public static String minWindow(String s, String t) {
        int n = s.length();
        int m = t.length();
        if (m > n) return "";
        if (m == n && s.equals(t)) return s;
        Map<Character, Integer> map = new HashMap<>();
        for (char c : t.toCharArray()) {
            map.put(c, map.getOrDefault(c, 0) + 1);
        }
        StringBuilder res = new StringBuilder();
        for (int i = 0; i <= n - m; i++) {
            Map<Character, Integer> clone = new HashMap<>(map);
            StringBuilder sb = new StringBuilder();
            for (int j = i; j < n; j++) {
                char ch = s.charAt(j);
                if (clone.isEmpty()) {
                    if (res.isEmpty()) res = sb;
                    else res = res.length() > sb.length() ? sb : res;
                    sb = new StringBuilder();
                    clone = new HashMap<>(map);
                } else if (clone.containsKey(ch)) {
                    int value = clone.get(ch) - 1;
                    if (value == 0) clone.remove(ch);
                    else clone.put(ch, value);
                    sb.append(s.charAt(j));
                } else sb.append(s.charAt(j));
            }
            if (clone.isEmpty()) {
                if (res.isEmpty()) res = sb;
                else res = res.length() > sb.length() ? sb : res;
            }

        }
        return res.toString();
    }

    static String minWindow2(String s, String t) {
        if (s == null || t == null || s.isEmpty()) return "";
        int left = 0;
        int right = 0;
        int[] foundMap = new int[256];
        int[] targetMap = new int[256];
        int countInRange = 0;
        int neededInRange = t.length();
        int minSize = Integer.MAX_VALUE;
        String result = "";
        // pre-process
        for (int i = 0; i < t.length(); i++) {
            char current = t.charAt(i);
            targetMap[current]++;
        }
        while (right < s.length()) {
            char c = s.charAt(right);
            if (targetMap[c] > 0) {
                if (foundMap[c] < targetMap[c]) {
                    countInRange++;
                }
                foundMap[c]++;
                // if all found in s, record window size
                if (countInRange == neededInRange) {
                    while (
                            left < s.length()
                                    &&
                                    targetMap[s.charAt(left)] == 0
                                    || (targetMap[s.charAt(left)] > 0 && foundMap[s.charAt(left)] > targetMap[s.charAt(left)])
                    ) {
                        foundMap[s.charAt(left)]--;
                        left++;
                    }
                    if (right - left + 1 < minSize) {
                        minSize = right - left + 1;
                        result = s.substring(left, right + 1);
                    }
                }
            }
            right++;
        }
        return result;
    }

    static String minWindow3(String s, String t) {
        int[] map = new int[128];
        for (char c : t.toCharArray()) map[c]++;
        int h = 0, e = 0, count = t.length(), head = 0, l = s.length() + 1;
        char[] sarr = s.toCharArray();
        while (e < sarr.length) {
            if (map[sarr[e++]]-- > 0) count--;
            while (count == 0) {
                if (e - h < l) {
                    head = h;
                    l = e - h;
                }
                if (map[sarr[h++]]++ == 0) count++;
            }
        }
        return l == s.length() + 1 ? "" : s.substring(head, head + l);
    }

    public static void main(String[] args) {
        String s = "ADOBECODEBANC";
        String t = "ABC";
        var res = minWindow(s, t);
        System.out.println(minWindow2(s, t));
        System.out.println(minWindow3(s, t));
        System.out.println(res);
        int[]arr={1};

    }
}
