package com.coding.hard;

class TreeNodeV2 {
    int val;
    TreeNodeV2 left;
    TreeNodeV2 right;

    TreeNodeV2() {
    }

    TreeNodeV2(int val) {
        this.val = val;
    }

    TreeNodeV2(int val, TreeNodeV2 left, TreeNodeV2 right) {
        this.val = val;
        this.left = left;
        this.right = right;
    }
}


public class BinaryTreeMaximumPathSum {
    int max = Integer.MIN_VALUE;

    public int maxPathSum(TreeNodeV2 root) {
        if (root == null)
            return 0;
        traverse(root);
        return max;
    }

    private int traverse(TreeNodeV2 root) {
        if (root == null)
            return 0;
        int leftChild = traverse(root.left);
        int rightChild = traverse(root.right);
        int max1 = Math.max(root.val, root.val + leftChild);
        int max2 = Math.max(root.val + rightChild, root.val + leftChild + rightChild);
        int localMax = Math.max(max1, max2);
        max = Math.max(localMax, max);
        return Math.max(root.val, Math.max(root.val + rightChild, root.val + leftChild));
    }

    void preOrder(TreeNodeV2 root) {
        if (root == null)
            return;
        preOrder(root.left);
        System.out.println(root.val);
        preOrder(root.right);
    }

    public static void main(String[] args) {
        TreeNodeV2 l1 = new TreeNodeV2(9);
        TreeNodeV2 rl1 = new TreeNodeV2(15);
        TreeNodeV2 rr1 = new TreeNodeV2(7);
        TreeNodeV2 r1 = new TreeNodeV2(20, rl1, rr1);
        TreeNodeV2 root = new TreeNodeV2(-10, l1, r1);
        System.out.println(root);
        var o = new BinaryTreeMaximumPathSum();
        var res = o.maxPathSum(root);
        System.out.println(res);
    }
}
