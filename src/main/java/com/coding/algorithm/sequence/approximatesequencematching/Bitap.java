package com.coding.algorithm.sequence.approximatesequencematching;

import java.util.Arrays;

public class Bitap {
    public static String search(String text, String pattern) {
        int m = pattern.length();
        long R;
        long[] patternMask = new long[Character.MAX_VALUE + 1];
        int i;
        if (pattern.isEmpty())
            return text;
        if (m > 31)
            return "Too long pattern";
        R = ~1L;
        String v = Long.toBinaryString(R);
        for (i = 0; i < patternMask.length; i++) {
            patternMask[i] = ~0L;
            String k = Long.toBinaryString(patternMask[i]);
            System.out.println();
        }
        for (i = 0; i < m; i++) {
            patternMask[pattern.charAt(i)] &= ~(1L << i);
            String l = Long.toBinaryString(patternMask[pattern.charAt(i)]);
            System.out.println();
        }
        for (i = 0; i < text.length(); i++) {
            R |= patternMask[text.charAt(i)];
            String s = Long.toBinaryString(R);
            R <<= 1;
            String j = Long.toBinaryString(R);
            if (0 == (R & (1L << m))) {
                return text.substring(i - m + 1, i + 1);
            }
        }
        return null;
    }

    public static String fuzzySearch(String text, String pattern, int k) {
        if (pattern.isEmpty())
            return text;
        if (pattern.length() > 63)
            return "Too long pattern";
        int m = pattern.length();
        long[] R = new long[k + 1];
        long[] patternMask = new long[Character.MAX_VALUE + 1];

        for (int i = 0; i <= k; i++) {
            R[i] = ~1L;
        }
        for (int i = 0; i <= Character.MAX_VALUE; i++) {
            patternMask[i] = ~0L;
        }
        for (int i = 0; i < m; i++) {
            patternMask[pattern.charAt(i)] &= ~(1L << i);
        }
        for (int i = 0; i < text.length(); i++) {
            long oldRd1 = R[0];
            R[0] |= patternMask[text.charAt(i)];
            R[0] <<= 1;
            for (int d = 1; d <= k; d++) {
                long tmp = R[d];
                R[d] = (oldRd1 & (R[d] | patternMask[text.charAt(i)])) << 1;
                oldRd1 = tmp;
            }
            if ((R[k] & (1L << m)) == 0)
                return text.substring(i - m + 1, i + 1);
        }
        return null;
    }


    public static void main(String[] args) {
        String text = "abc123";
        String pattern = "abc";
        System.out.println(search(text, pattern));
    }
}
