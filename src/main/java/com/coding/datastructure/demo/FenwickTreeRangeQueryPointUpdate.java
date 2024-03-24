package com.coding.datastructure.demo;


import java.util.Arrays;

public class FenwickTreeRangeQueryPointUpdate {
    final int N;
    private long[] tree;

    public FenwickTreeRangeQueryPointUpdate(int sz) {
        tree = new long[(N = sz + 1)];
    }

    public FenwickTreeRangeQueryPointUpdate(long[] values) {
        if (values == null)
            throw new IllegalArgumentException();
        N = values.length;
        values[0] = 0L;
        tree = values.clone();
        for (int i = 1; i < N; i++) {
            int parent = i + lsb(i);
            if (parent < N)
                tree[parent] += tree[i];
        }
    }

    private static int lsb(int i) {
        return i & -i;
    }

    private long prefixSum(int i) {
        long sum = 0L;
        while (i != 0) {
            sum += tree[i];
            i &= ~lsb(i);
        }
        return sum;
    }

    private long sum(int left, int right) {
        if (right < left)
            throw new IllegalArgumentException();
        return prefixSum(right) - prefixSum(left);
    }

    public long get(int i) {
        return sum(i, i);
    }

    public void add(int i, long v) {
        while (i < N) {
            tree[i] += v;
            i += lsb(i);
        }
    }

    public void set(int i, long v) {
        add(i, v - sum(i, i));
    }

    @Override
    public String toString() {
        return Arrays.toString(tree);
    }
}
