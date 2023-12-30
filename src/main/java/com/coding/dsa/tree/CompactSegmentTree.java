package com.coding.dsa.tree;

import javax.sound.midi.MidiFileFormat;
import java.util.Arrays;

public class CompactSegmentTree {
    private int N;
    private long UNIQUE = 8123572096793136074L;
    private long[] tree;

    public CompactSegmentTree(int size) {
        tree = new long[2 * (N = size)];
        Arrays.fill(tree, UNIQUE);
    }

    public CompactSegmentTree(long[] values) {
        this(values.length);
        for (int i = 0; i < N; i++) {
            modify(i, values[i]);
        }
    }

    private long function(long a, long b) {
        if (a == UNIQUE)
            return b;
        else if (b == UNIQUE)
            return a;
        return a + b;
    }

    public void modify(int i, long value) {
        tree[i + N] = function(tree[i + N], value);
        for (i += N; i > 1; i >>= 1) {
            tree[i >> 1] = function(tree[i], tree[i ^ 1]);
        }
    }

    public long query(int l, int r) {
        long res = UNIQUE;
        for (l += N, r += N; l < r; l >>= 1, r >>= 1) {
            if ((l & 1) != 0)
                res = function(res, tree[l++]);
            if ((r & 1) != 0)
                res = function(res, tree[--r]);
        }
        if (res == UNIQUE)
            throw new IllegalStateException();

        return res;
    }

    public static void main(String[] args) {
        long[] values = {1, 2, 3, 1, 5, 7, 2, 1, 5, 2, 3, 1};
        CompactSegmentTree segmentTree = new CompactSegmentTree(values);
        System.out.println(segmentTree.query(0, 3));

    }
}
