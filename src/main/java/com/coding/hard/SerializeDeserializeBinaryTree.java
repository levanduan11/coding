package com.coding.hard;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringJoiner;

public class SerializeDeserializeBinaryTree {
    public static void main(String[] args) {
        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(2);
        root.right = new TreeNode(3);
        root.right.left = new TreeNode(4);
        root.right.right = new TreeNode(5);
        Codec codec = new Codec();
        String enc = codec.serialize(root);
        TreeNode dec = codec.deserialize(enc);
        System.out.println(dec);
    }
}

class Codec {

    // Encodes a tree to a single string.
    public String serialize(TreeNode root) {
        String res = preorder(root);
        return res.substring(0,res.length()-1);
    }

    // Decodes your encoded data to tree.
    public TreeNode deserialize(String data) {
        Queue<String> nodes = new LinkedList<>(Arrays.asList(data.split(",")));
        TreeNode res = buildTree(nodes);
        return res;
    }

    private TreeNode buildTree(Queue<String> nodes) {
        String val = nodes.poll();
        if ("null".equals(val) || val == null)
            return null;
        TreeNode node = new TreeNode(Integer.parseInt(val));
        node.left = buildTree(nodes);
        node.right = buildTree(nodes);
        return node;
    }

    public String preorder(TreeNode root) {
        if (root == null)
            return "null,";
        StringBuilder sb = new StringBuilder();
        sb.append(root.val).append(',');
        String left = preorder(root.left);
        sb.append(left);
        String right = preorder(root.right);
        sb.append(right);
        return sb.toString();
    }
}

class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;

    TreeNode(int x) {
        val = x;
    }
}
