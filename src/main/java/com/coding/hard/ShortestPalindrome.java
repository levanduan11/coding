package com.coding.hard;

import java.util.List;
import java.util.stream.Stream;

public class ShortestPalindrome {
    public static String shortestPalindrome(String s) {
        if (s == null || s.isEmpty())
            return "";
        int i = 0, n = s.length();
        for (int j = n - 1; j >= 0; j--) {
            char c = s.charAt(i);
            char c1 = s.charAt(j);
            if (c == c1)
                ++i;
        }
        if (i == n)
            return s;
        String remaining = s.substring(i);
        String rem_rev = new StringBuilder(remaining).reverse().toString();
        return rem_rev + shortestPalindrome(s.substring(0, i)) + remaining;
    }

    static String shortestPalindrome2(String s) {
        int base = 131, mul = 1, mod = (int) 1e9 + 7, prefix = 0, suffix = 0, idx = 0, n = s.length();
        for (int i = 0; i < n; i++) {
            int t = s.charAt(i) - 'a' + 1;
            prefix = (int) (((long) prefix * base + t) % mod);
            suffix = (int) ((suffix + (long) t * mul) % mod);
            mul = (int) (((long) mul * base) % mod);
            if (prefix == suffix)
                idx = i + 1;
        }
        if (idx == n)
            return s;
        return new StringBuilder(s.substring(idx)).reverse().toString()+s;
    }

    public static void main(String[] args) {
        Stream.of(new String[]{"aacecaaa", "aaacecaaa"}, new String[]{"abcd", "dcbabcd"})
                .map(strings -> new String[]{shortestPalindrome2(strings[0]), strings[1]})
                .map(strings -> strings[0].equals(strings[1]))
                .forEach(System.out::println);


    }
}
