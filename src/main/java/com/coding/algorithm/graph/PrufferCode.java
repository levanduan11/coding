package com.coding.algorithm.graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PrufferCode {
    public static List<Integer> encode(int[] tree) {
        int n = tree.length + 2;
        int[] degree = new int[n];
        for (int vertex : tree) {
            degree[vertex - 1]++;
        }
        List<Integer> pufferSequence = new ArrayList<>();
        for (int i = 0; i < n - 2; i++) {
            int leaf = findLeaf(degree);
            if (leaf == -1){
                System.err.println("invalid index");
                return null;
            }
            pufferSequence.add(tree[leaf] - 1);
            degree[tree[leaf] - 1]--;
            degree[leaf]--;
        }
        return pufferSequence;
    }

    private static int findLeaf(int[] degree) {
        for (int i = 0; i < degree.length; i++) {
            if (degree[i] == 1)
                return i;
        }
        return -1;
    }

    public static int[] decode(List<Integer> prufferSequence) {
        if (prufferSequence == null)
            return null;
        int n = prufferSequence.size() + 2;
        int[] degree = new int[n];
        for (int vertex : prufferSequence) {
            degree[vertex]++;
        }
        int[] tree = new int[n - 2];
        int leaf = findLeaf(degree);
        if (leaf == -1)
            return null;
        for (int i = 0; i < n - 2; i++) {
            tree[i] = leaf + 1;
            degree[leaf]--;
            degree[prufferSequence.get(i)]--;
            leaf = findLeaf(degree);
        }
        int j = 0;
        for (int i = 0; i < n; i++) {
            if (degree[i] == 1)
                tree[j++] = i + 1;
        }
        return tree;
    }

    public static void main(String[] args) {
        int[]labeledTree = {2,3,4,5,1,6};
        List<Integer> pufferSequence = encode(labeledTree);
        System.out.println("sequence: "+pufferSequence);
        int[] decode = decode(pufferSequence);
        System.out.println("decode: "+ Arrays.toString(decode));
    }
}
