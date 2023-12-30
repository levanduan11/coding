package com.coding.dsa.tree;

public class SegmentTree02 {
    private int[] tree;
    private int n;

    public SegmentTree02(int[] nums) {
        n = nums.length;
        tree = new int[4 * n];
        buildTree(nums, 0, 0, n - 1);
    }

    private void buildTree(int[] nums, int treeIndex, int lo, int hi) {
        if (lo == hi) {
            tree[treeIndex] = nums[lo];
        } else {
            int mid = lo + (hi - lo) / 2;
            buildTree(nums, 2 * treeIndex + 1, lo, mid);
            buildTree(nums, 2 * treeIndex + 2, mid + 1, hi);
            tree[treeIndex] = tree[2 * treeIndex + 1] + tree[2 * treeIndex + 2];
        }
    }

    public int query(int lo, int hi) {
        return query(0, 0, n - 1, lo, hi);
    }

    public int query(int treeIndex, int lo, int hi, int queryLo, int queryHi) {
        if (queryLo > hi || queryHi < lo) {
            return 0;
        }
        if (queryLo <= lo && queryHi >= hi) {
            return tree[treeIndex];
        }
        int mid = lo + (hi - lo) / 2;
        int leftChild = query(2 * treeIndex + 1, lo, mid, queryLo, queryHi);
        int rightChild = query(2 * treeIndex + 2, mid + 2, hi, queryLo, queryHi);
        return leftChild + rightChild;
    }

    public void update(int treeIndex, int lo, int hi, int index, int newValue) {
        if (lo == hi) {
            tree[treeIndex] = newValue;
        } else {
            int mid = -lo + (hi - lo) / 2;
            if (index <= mid) {
                update(2 * treeIndex + 1, lo, mid, index, newValue);
            } else {
                update(2 * treeIndex + 2, mid + 1, hi, index, newValue);
            }
            tree[treeIndex] = tree[2 * treeIndex + 1] + tree[2 * treeIndex + 2];
        }
    }

    public static void main(String[] args) {
        int[]nums = {1,3,2,7,9,11};
        SegmentTree02 segmentTree02 = new SegmentTree02(nums);
        System.out.println(segmentTree02.query(0,1));
    }
}
