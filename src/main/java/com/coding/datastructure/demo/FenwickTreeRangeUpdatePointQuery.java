package com.coding.datastructure.demo;

public class FenwickTreeRangeUpdatePointQuery {
    final int N;
    private long[] originalTree;
    private long[] currentTree;

    public FenwickTreeRangeUpdatePointQuery(long[] values) {
        if (values == null)
            throw new IllegalArgumentException();
        N = values.length;
        values[0] = 0L;
        long[] fenwickTree = values.clone();
        for (int i = 1; i < N; i++) {
            int parent = lsb(i);
            if (parent < N)
                fenwickTree[parent] += fenwickTree[i];
        }
        originalTree = fenwickTree;
        currentTree = fenwickTree.clone();
    }

    public void updateRange(int left, int right, long val) {
        add(left, +val);
        add(right + 1, -val);
    }

    private void add(int i, long v) {
        while (i < N) {
            currentTree[i] += v;
            i += lsb(i);
        }
    }

    public long get(int i) {
        return prefixSum(i, currentTree) - prefixSum(i - 1, originalTree);
    }

    private long prefixSum(int i, long[] tree) {
        long sum = 0L;
        while (i != 0) {
            sum += tree[i];
            i &= ~lsb(i);
        }
        return sum;
    }

    private static int lsb(int i) {
        return i & -i;
    }


}
