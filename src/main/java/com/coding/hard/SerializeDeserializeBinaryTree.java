package com.coding.hard;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class SerializeDeserializeBinaryTree {
    public static void main(String[] args) {
        TreeNodeV2 root = new TreeNodeV2(1);
        root.left = new TreeNodeV2(2);
        root.right = new TreeNodeV2(3);
        root.right.left = new TreeNodeV2(4);
        root.right.right = new TreeNodeV2(5);
        Codec codec = new Codec();
        String enc = codec.serialize(root);
        TreeNodeV2 dec = codec.deserialize(enc);
        System.out.println(dec);
    }
}

class Codec {

    // Encodes a tree to a single string.
    public String serialize(TreeNodeV2 root) {
        String res = preorder(root);
        return res.substring(0,res.length()-1);
    }

    // Decodes your encoded data to tree.
    public TreeNodeV2 deserialize(String data) {
        Queue<String> nodes = new LinkedList<>(Arrays.asList(data.split(",")));
        TreeNodeV2 res = buildTree(nodes);
        return res;
    }

    private TreeNodeV2 buildTree(Queue<String> nodes) {
        String val = nodes.poll();
        if ("null".equals(val) || val == null)
            return null;
        TreeNodeV2 node = new TreeNodeV2(Integer.parseInt(val));
        node.left = buildTree(nodes);
        node.right = buildTree(nodes);
        return node;
    }

    public String preorder(TreeNodeV2 root) {
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
    TreeNodeV2 left;
    TreeNodeV2 right;

    TreeNode(int x) {
        val = x;
    }
}
