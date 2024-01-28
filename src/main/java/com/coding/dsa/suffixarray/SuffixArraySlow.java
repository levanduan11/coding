package com.coding.dsa.suffixarray;

import java.util.Arrays;

public class SuffixArraySlow extends SuffixArray {
    private static class Suffix implements Comparable<Suffix> {
        final int index, len;
        final int[] text;

        public Suffix(int[] text, int index) {
            this.len = text.length - index;
            this.index = index;
            this.text = text;
        }

        @Override
        public int compareTo(Suffix other) {
            if (this == other)
                return 0;
            int min_len = Math.min(len, other.len);
            for (int i = 0; i < min_len; i++) {
                if (text[index + i] < other.text[other.index + i])
                    return -1;
                if (text[index + i] > other.text[other.index + i])
                    return +1;
            }
            return len - other.len;
        }

        @Override
        public String toString() {
            return new String(text, index, len);
        }
    }

    Suffix[] suffixes;

    public SuffixArraySlow(String text) {
        super(toIntArray(text));
    }

    public SuffixArraySlow(int[] text) {
        super(text);
    }

    @Override
    protected void construct() {
        sa = new int[N];
        suffixes = new Suffix[N];
        for (int i = 0; i < N; i++) {
            suffixes[i] = new Suffix(T, i);
        }
        Arrays.sort(suffixes);
        for (int i = 0; i < N; i++) {
            Suffix suffix = suffixes[i];
            sa[i] = suffix.index;
            suffixes[i] = null;
        }
        suffixes = null;
    }
}
