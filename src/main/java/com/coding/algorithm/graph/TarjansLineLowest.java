package com.coding.algorithm.graph;

import java.util.Objects;

public class TarjansLineLowest {
    static final int V = 5;
    static final int WHITE = 1;
    static final int BLACK = 2;

    static class Node {
        int data;
        Node left, right;
    }

    static class Subset {
        int parent;
        int rank;
        int ancestor;
        int child;
        int sibling;
        int color;
    }

    static class Query {
        int L, R;

        Query(int L, int R) {
            this.L = L;
            this.R = R;
        }
    }

    static Node newNode(int data) {
        Node node = new Node();
        node.data = data;
        node.left = node.right = null;
        return node;
    }

    static void makeSet(Subset[] subsets, int i) {
        if (i < 1 || i > V)
            return;
        subsets[i].color = WHITE;
        subsets[i].parent = i;
        subsets[i].rank = 0;
    }

    static int findSet(Subset[] subsets, int i) {
        if (subsets[i].parent != i) {
            int parent = findSet(subsets, subsets[i].parent);
            subsets[i].parent = parent;
        }
        return subsets[i].parent;
    }

    static void unionSet(Subset[] subsets, int x, int y) {
        int xRoot = findSet(subsets, x);
        int yRoot = findSet(subsets, y);
        if (subsets[xRoot].rank < subsets[yRoot].rank)
            subsets[xRoot].parent = yRoot;
        else if (subsets[xRoot].rank > subsets[yRoot].rank)
            subsets[yRoot].parent = xRoot;
        else {
            subsets[yRoot].parent = xRoot;
            subsets[xRoot].rank++;
        }
    }

    static void lcaWalk(int u, Query[] q, int m, Subset[] subsets) {
        makeSet(subsets, u);
        subsets[findSet(subsets, u)].ancestor = u;
        int child = subsets[u].child;
        while (child != 0) {
            lcaWalk(child, q, m, subsets);
            unionSet(subsets, u, child);
            subsets[findSet(subsets, u)].ancestor = u;
            child = subsets[child].sibling;
        }
        subsets[u].color = BLACK;
        for (int i = 0; i < m; i++) {
            if (q[i].L == u) {
                if (subsets[q[i].R].color == BLACK) {
                    System.out.printf("LCA (%d %d)->%d\n",
                            q[i].L,
                            q[i].R,
                            subsets[findSet(subsets, q[i].R)].ancestor);
                }
            } else if (q[i].R == u) {
                if (subsets[q[i].L].color == BLACK) {
                    System.out.printf("LCA (%d %d)->%d\n",
                            q[i].L,
                            q[i].R,
                            subsets[findSet(subsets, q[i].L)].ancestor);
                }
            }
        }
    }

    static void preprocess(Node node, Subset[] subsets) {
        if (node == null)
            return;
        preprocess(node.left, subsets);
        if (node.left != null && node.right != null) {
            subsets[node.data].child = node.left.data;
            subsets[node.left.data].sibling = node.right.data;
        }else if (node.left != null || node.right != null){
            subsets[node.data].child = Objects.requireNonNullElseGet(node.left, () -> node.right).data;
        }
        preprocess(node.right,subsets);
    }
    static void initialise(Subset[]subsets){
        for (int i = 1; i < subsets.length; i++) {
            subsets[i] = new Subset();
            subsets[i].color = WHITE;
        }
    }
    static void printLCAs(Node root,Query[]q,int m){
        Subset[]subsets = new Subset[V + 1];
        initialise(subsets);
        preprocess(root,subsets);
        lcaWalk(root.data, q,m,subsets);
    }

    public static void main(String[] args) {
        Node root = newNode(1);
        root.left = newNode(2);
        root.right= newNode(3);
        root.left.left = newNode(4);
        root.left.right = newNode(5);
        Query[]q = new Query[3];
        q[0] = new Query(5,4);
        q[1] = new Query(1,3);
        q[2] = new Query(2,3);
        int m = q.length;
        printLCAs(root,q,m);
    }
}
