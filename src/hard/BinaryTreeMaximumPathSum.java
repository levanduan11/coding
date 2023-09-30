package hard;

class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;

    TreeNode() {
    }

    TreeNode(int val) {
        this.val = val;
    }

    TreeNode(int val, TreeNode left, TreeNode right) {
        this.val = val;
        this.left = left;
        this.right = right;
    }
}


public class BinaryTreeMaximumPathSum {
    int max = Integer.MIN_VALUE;

    public int maxPathSum(TreeNode root) {
        if (root == null)
            return 0;
        traverse(root);
        return max;
    }

    private int traverse(TreeNode root) {
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

    void preOrder(TreeNode root) {
        if (root == null)
            return;
        preOrder(root.left);
        System.out.println(root.val);
        preOrder(root.right);
    }

    public static void main(String[] args) {
        TreeNode l1 = new TreeNode(9);
        TreeNode rl1 = new TreeNode(15);
        TreeNode rr1 = new TreeNode(7);
        TreeNode r1 = new TreeNode(20, rl1, rr1);
        TreeNode root = new TreeNode(-10, l1, r1);
        System.out.println(root);
        var o = new BinaryTreeMaximumPathSum();
        var res = o.maxPathSum(root);
        System.out.println(res);
    }
}
