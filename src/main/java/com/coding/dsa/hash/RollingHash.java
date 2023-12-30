package com.coding.dsa.hash;

import java.util.ArrayList;
import java.util.List;

public class RollingHash {
    static List<Integer> rollingHash(String s, int windowSize, int base, int mod) {
        int n = s.length();
        List<Integer> power = new ArrayList<>(n + 1);
        List<Integer> hashValues = new ArrayList<>(n - windowSize + 1);
        for (int i = 0; i <= n; i++) {
            power.add(1);
        }
        for (int i = 1; i <= n; i++) {
            Integer i1 = power.get(i - 1);
            int element = (i1 * base) % mod;
            power.set(i, element);
        }
        int currentHash = 0;
        for (int i = 0; i < windowSize; i++) {
            currentHash = (currentHash * base + s.charAt(i)) % mod;
        }
        hashValues.add(currentHash);
        for (int i = 1; i <= n - windowSize; i++) {
            Integer i2 = power.get(windowSize - 1);
            int i1 = i2 * s.charAt(i - 1);
            currentHash = (currentHash - i1) % mod;
            currentHash = (currentHash * base + s.charAt(i + windowSize - 1)) % mod;
            hashValues.add(currentHash);
        }
        return hashValues;
    }

    public static void main(String[] args) {
        String input = "abcabcabc";
        int windowSize = 3;
        var res = rollingHash(input, windowSize, 26, 1000000007);
        System.out.println(res);
    }
}
