package com.coding.dsa.tree;

import java.util.Arrays;

import static java.util.stream.IntStream.range;

public class SegmentTree {
    private Node[] heap;
    private int[] array;
    private int size;

    public SegmentTree(int[] array) {
        this.array = Arrays.copyOf(array, array.length);
        // size = 2 * 2 ^ log2(n) + 1
        size = (int) (2 * Math.pow(2.0, Math.floor(Math.log(array.length) / Math.log(2.0)) + 1));
        heap = new Node[size];
        build(1, 0, array.length);
    }

    public int size() {
        return array.length;
    }

    private void build(int v, int from, int size) {
        heap[v] = new Node();
        heap[v].from = from;
        heap[v].to = from + size - 1;
        if (size == 1) {
            heap[v].sum = array[from];
            heap[v].min = array[from];
        } else {
            build(2 * v, from, size / 2);
            build(2 * v + 1, from + size / 2, size - size / 2);
            heap[v].sum = heap[2 * v].sum + heap[2 * v + 1].sum;
            heap[v].min = Math.min(heap[2 * v].min, heap[2 * v + 1].min);
        }
    }

    public int rsq(int from, int to) {
        return rsq(1, from, to);
    }

    private int rsq(int v, int from, int to) {
        Node n = heap[v];
        if (n.pendingVal != null && contains(n.from, n.to, from, to))
            return (to - from + 1) * n.pendingVal;
        if (contains(from, to, n.from, n.to))
            return heap[v].sum;
        if (intersects(from, to, n.from, n.to)) {
            propagate(v);
            int leftSum = rsq(2 * v, from, to);
            int rightSum = rsq(2 * v + 1, from, to);
            return leftSum + rightSum;
        }
        return 0;
    }

    public int rMinQ(int from, int to) {
        return rMinQ(1, from, to);
    }

    private int rMinQ(int v, int from, int to) {
        Node n = heap[v];
        if (n.pendingVal != null && contains(n.from, n.to, from, to))
            return n.pendingVal;
        if (contains(from, to, n.from, n.to))
            return heap[v].min;
        if (intersects(from, to, n.from, n.to)) {
            propagate(v);
            int leftMin = rMinQ(2 * v, from, to);
            int rightMin = rMinQ(2 * v + 1, from, to);
            return Math.min(leftMin, rightMin);
        }
        return Integer.MAX_VALUE;
    }

    public void update(int from, int to, int value) {
        update(1, from, to, value);
    }

    private void update(int v, int from, int to, int value) {
        Node n = heap[v];
        if (contains(from, to, n.from, n.to))
            change(n, value);
        if (n.size() == 1)
            return;
        if (intersects(from, to, n.from, n.to)) {
            propagate(v);
            update(2 * v, from, to, value);
            update(2 * v + 1, from, to, value);
            n.sum = heap[2 * v].sum + heap[2 * v + 1].sum;
            n.min = Math.min(heap[2 * v].min, heap[2 * v + 1].min);
        }
    }

    private void propagate(int v) {
        Node n = heap[v];
        if (n.pendingVal != null) {
            change(heap[2 * v], n.pendingVal);
            change(heap[2 * v + 1], n.pendingVal);
            n.pendingVal = null;
        }
    }

    private void change(Node n, int value) {
        n.pendingVal = value;
        n.sum = n.size() * value;
        n.min = value;
        array[n.from] = value;
    }

    private boolean contains(int from1, int to1, int from2, int to2) {
        return from2 >= from1 && to2 <= to1;
    }

    private boolean intersects(int from1, int to1, int from2, int to2) {
        return from1 <= from2 && to1 >= from2
                || from1 >= from2 && from1 <= to2;
    }

    public static void main(String[] args) {
        int[] input = {1,2,3,4,5,6,7,8,9,10};
        SegmentTree segmentTree = new SegmentTree(input);
        int res = segmentTree.rsq(0, 3);
        System.out.println(res);
    }
    static class Node {
        int sum;
        int min;
        Integer pendingVal = null;
        int from;
        int to;

        int size() {
            return to - from + 1;
        }
    }
}
